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
		for (Node s : nodes){ 
			System.out.println(s.getValue());
			Object[] pre = s.pred().toArray();
			Object[] suc = s.succ().toArray();
			System.out.println("Pred");
			for(int x = 0; x < pre.length; x++){
				System.out.println(((Node)pre[x]).getValue());
			}
			System.out.println("Succ");
			for(int x = 0; x < suc.length; x++){
				System.out.println(((Node)suc[x]).getValue());
			}
			System.out.println();
		}
	}



}
