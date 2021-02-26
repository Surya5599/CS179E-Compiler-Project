import cs132.util.IndentPrinter;
import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VAddr;
import cs132.vapor.ast.VAssign;
import cs132.vapor.ast.VBranch;
import cs132.vapor.ast.VBuiltIn;
import cs132.vapor.ast.VCall;
import cs132.vapor.ast.VCodeLabel;
import cs132.vapor.ast.VDataSegment;
import cs132.vapor.ast.VFunction;
import cs132.vapor.ast.VGoto;
import cs132.vapor.ast.VInstr;
import cs132.vapor.ast.VLabelRef;
import cs132.vapor.ast.VMemRead;
import cs132.vapor.ast.VMemWrite;
import cs132.vapor.ast.VOperand;
import cs132.vapor.ast.VReturn;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.VAddr.Var;
import cs132.vapor.ast.VBuiltIn.Op;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.time.YearMonth;

public class V2VM {

	public static void main(String[] args) throws IOException {
		VaporProgram vp = parseVapor(System.in, System.err);
		VDataSegment[] vData = vp.dataSegments;
		VOperand[] methods = vData[0].values;
		VFunction[] vfunc = vp.functions;
		TranslateVisitor tp = new TranslateVisitor();
		convertDataSegments(vData);
		convertFunctions(vfunc);
		System.out.println("hi");
	}

	public static void convertDataSegments(VDataSegment[] vData) throws IOException {

	}

	public static void convertFunctions(VFunction[] vf) throws IOException {

		for (VFunction x : vf) {
			Graph g = createCFG(x);
		}
		System.out.println("hi");
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
		cfg.show();
		return cfg;
	}

	

	private static void setVariableInfo(VInstr y, Node curr) {
		if(y instanceof VAssign){
			curr.def.add(((VAssign)y).dest.toString());
		}
		else if(y instanceof VBranch){
			curr.def.add(((VBranch)y).value.toString());
		}
		else if(y instanceof VBuiltIn){
			if(((VBuiltIn)y).dest != null)
				curr.def.add(((VBuiltIn)y).dest.toString());
		}
		else if(y instanceof VCall){

		}
		else if(y instanceof VGoto){

		}
		else if(y instanceof VMemRead){

		}
		else if(y instanceof VMemWrite){
			curr.def.add(((VMemWrite)y).base.var.toString());
		}
		else if(y instanceof VReturn){

		}
	}

	public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException {
		Op[] ops = {
			Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
			Op.PrintIntS, Op.HeapAllocZ, Op.Error,
		};
		boolean allowLocals = true;
		String[] registers = null;
		boolean allowStack = false;
	
		VaporProgram tree;
		try {
			tree = VaporParser.run(new InputStreamReader(in), 1, 1,
														 java.util.Arrays.asList(ops),
														 allowLocals, registers, allowStack);
		}
		catch (ProblemException ex) {
			err.println(ex.getMessage());
			return null;
		}
	
		return tree;
	}

}