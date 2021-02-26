

public class Graph {
private NodeList cfg;
 public Graph(){
	this.cfg = new NodeList();
 }
 public NodeList nodes(){
	return this.cfg;
 }
 public Node newNode(int x){
	 Node n = new Node(x);
	 cfg.addNode(n);
	return n;
 }

 public Node getNode(int x){
	 return cfg.find(x);
 }

 public void addEdge(Node from, Node to){
	from.succ().addNode(to);
	to.pred().addNode(from);
 }
 public void rmEdge(Node from, Node to){

 }
 public void show(){
	cfg.print();
 }
}


