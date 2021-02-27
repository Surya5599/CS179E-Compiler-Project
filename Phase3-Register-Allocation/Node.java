import java.util.HashSet;

public class Node {

	public NodeList succesors;
	public NodeList predecessors;
	private int outD;
	private int inD;
	private int value;
	public HashSet<String> in;
	public HashSet<String> out;
	public HashSet<String> def;
	public HashSet<String> use;

	public Node(int x){
		this.value = x;
		this.succesors = new NodeList();
		this.predecessors = new NodeList();
		this.in = new HashSet<String>();
		this.out = new HashSet<String>();
		this.def = new HashSet<String>();
		this.use = new HashSet<String>();
	}

	public int getValue(){
		return this.value;
	}
	public NodeList succ(){
		return this.succesors;
	}
	public NodeList pred(){
		return this.predecessors;
	}

	public NodeList adj(){
		return null;
	}
	public int outDegree(){
		return succesors.size();
	}
	public int inDegree(){
		return predecessors.size();
	}
	public int degree(){
		return null;
	}
	public boolean goesTo(Node n){
		return null;
	}
	public boolean comesFrom(Node n){
		return null;
	}
	public boolean adj(Node n){
		return null;
	}
	public String toString(){
		return null;
	}
 }