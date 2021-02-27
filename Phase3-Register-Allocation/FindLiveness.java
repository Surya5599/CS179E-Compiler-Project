import java.util.*;

import cs132.vapor.ast.*;

public class FindLiveness {
	VFunction function;
	public FindLiveness(VFunction x){
		this.function = x;
	}

	public HashMap<String, LivenessInterval> LivenessAnalysis(){
		Graph g = createCFG(function); //create the CFG
		HashMap<String, LivenessInterval> li = findLiveTime(g);
		return li;
	}

	private static HashMap<String, LivenessInterval> findLiveTime(Graph cfg) {
		HashMap<String, LivenessInterval> li = new HashMap<String, LivenessInterval>();
		List<Integer> key = cfg.getAllKey();
		for (Integer i : key){
			Node n = cfg.getNode(i);
			n.actives.addAll(n.in);
			n.actives.addAll(n.def);
		}
		for (Integer i : key){
			for(String s: cfg.getNode(i).actives){
				if(li.containsKey(s)){
					li.get(s).setStop(i);
				}
				else{
					li.put(s, new LivenessInterval(s, i, i));
				}
			}
		}
           
		return li;
	}

	private static Graph createCFG(VFunction x) {
		Graph cfg = new Graph();
		for (VInstr y : x.body) {
			cfg.newNode(y.sourcePos.line);
		}
		Node curr;
		Node prev = null;
		for (int index = 0; index < x.body.length; index++) {
			VInstr y = x.body[index];
			curr = cfg.getNode(y.sourcePos.line);
			setVariableInfo(y, curr);
			if (y instanceof VGoto) {
				cfg.addEdge(prev, curr);
				VAddr<VCodeLabel> lab = ((VGoto)y).target;
				VAddr.Label<VCodeLabel> l = ((VAddr.Label<VCodeLabel>)lab);
				VCodeLabel codeLab = l.label.getTarget();
				Node jumpTo = cfg.getNode(codeLab.sourcePos.line + 1);
				cfg.addEdge(curr, jumpTo);
			}
			else if(y instanceof VBranch){
				cfg.addEdge(prev, curr);
				VCodeLabel codeLab = ((VBranch)y).target.getTarget();
				Node jumpTo = cfg.getNode(codeLab.sourcePos.line + 1);
				cfg.addEdge(curr, jumpTo);
				prev = curr;
			}
			else{
					boolean checkgoto = true;
					if(index != 0){
						checkgoto = !(x.body[index - 1] instanceof VGoto);
					}
					if(checkgoto){
						if(prev != null ){
							cfg.addEdge(prev, curr);
						}
					}
					prev = curr;
			}
		}
		findInOut(x, cfg);
		//cfg.show();
		return cfg;
	}

	private static void findInOut(VFunction x, Graph cfg){
		for (VInstr y : x.body) {
			Node n = cfg.getNode(y.sourcePos.line);
			n.in.clear();
			n.out.clear();
			n.inoutCheck = false;
		}
		do{
			for (VInstr y : x.body) {
				Node n = cfg.getNode(y.sourcePos.line);
				HashSet<String> temp_in = new HashSet<String>(n.in);
				HashSet<String> temp_out = new HashSet<String>(n.out);
				n.in.addAll(n.out);
				n.in.removeAll(n.def);
				n.in.addAll(n.use);
				Object[] suc = n.succ().toArray();
				for(int i = 0; i < suc.length; i++){
					int sucVal = ((Node)suc[i]).getValue();
					Node sucNode = cfg.getNode(sucVal);
					n.out.addAll(sucNode.in);
				}
				if(temp_in.equals(n.in) && temp_out.equals(n.out)){
					n.inoutCheck = true;
				}
			}
		}while(!checkDone(cfg));
	}

	public static boolean checkDone(Graph cfg) {
		Object[] Nodes = cfg.nodes().toArray();
		boolean done = true;
		for(int i = 0; i < Nodes.length; i++){
			if(((Node)Nodes[i]).inoutCheck == false){
				done = false;
			}
		}
		return done;
	}

	

	private static void setVariableInfo(VInstr y, Node curr) {
		if(y instanceof VAssign){
			curr.def.add(((VAssign)y).dest.toString());
		}
		else if(y instanceof VBranch){
			curr.use.add(((VBranch)y).value.toString());
		}
		else if(y instanceof VBuiltIn){
			VBuiltIn x = ((VBuiltIn)y);
			if(x.dest != null){
				curr.def.add(((VBuiltIn)y).dest.toString());
			}
			for(VOperand vO: x.args){
				if(vO instanceof	VVarRef){
					curr.use.add(vO.toString());
				}
			}
			
		}
		else if(y instanceof VCall){
			VCall x = ((VCall)y);
			if(x.dest != null){
				curr.def.add(((VCall)y).dest.toString());
			}
			curr.use.add(x.addr.toString());
			for(VOperand vO: x.args){
				if(vO instanceof	VVarRef){
					curr.use.add(vO.toString());
				}
			}
		}
		else if(y instanceof VMemRead){
			VMemRead read = (VMemRead)y;
			curr.def.add(read.dest.toString());
			if(read.source instanceof VMemRef.Global){
				VMemRef.Global vmem = (VMemRef.Global)read.source;
				curr.use.add(vmem.base.toString());
			}
		}
		else if(y instanceof VMemWrite){
			VMemWrite x = ((VMemWrite)y);
			if(x.dest instanceof VMemRef.Global){
				VMemRef.Global vmem = (VMemRef.Global)x.dest;
				curr.use.add(vmem.base.toString());
			}
		}
		else if(y instanceof VReturn){
			VReturn ret = ((VReturn)y);
			if(ret.value != null){
				if(ret.value instanceof VVarRef){
					curr.use.add(ret.value.toString());
				}
				//curr.use.add(ret.value);
			}		
		}
	}
}