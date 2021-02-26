	public abstract class FlowGraph extends Graph {
	public abstract TempList def(Node node){
		return null;
	}
	public abstract TempList use(Node node){
		return null;
	}
	public abstract boolean isMove(Node node){
		return null;
	}
	//public void show(java.io.PrintStream out);
	} 
