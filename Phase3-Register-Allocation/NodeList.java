import java.util.LinkedHashSet;

public class NodeList {
	private LinkedHashSet<Node> nodes;
	
	public NodeList(){
		nodes = new LinkedHashSet<Node>();
	}

	public void addNode(Node n){
		nodes.add(n);
	}

	public int size(){
		return nodes.size();
	}

	public void removeNode(Node n){
		nodes.remove(n);
	}
	public Node find(int x){
		Node selectNode = null;
		for (Node s : nodes){ 
        if(s.getValue() == x){
					selectNode =  s;
				}
		}
		return selectNode;
	}

	public Object[] toArray(){
		return nodes.toArray();
	}
	public void print(){
		String output = "";
		for (Node s : nodes){ 
			output = output + s.getValue() + "\n";
			Object[] pre = s.pred().toArray();
			Object[] suc = s.succ().toArray();
			output += "Pred: ";
			for(int x = 0; x < pre.length; x++){
				output += ((Node)pre[x]).getValue() + ",";
			}
			output += "\n";
			output += "Succ: ";
			for(int x = 0; x < suc.length; x++){
				output += ((Node)suc[x]).getValue() + ",";
			}
			output += "\n";
			output += "Use: " + s.use.toString();
			output += "\n";
			output += "Def: " + s.def.toString();
			output += "\n \n";
		}
		System.out.println(output);
	}



}
