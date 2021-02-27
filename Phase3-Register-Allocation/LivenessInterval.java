public class LivenessInterval{
	public String variable;
	private int start;
	private int stop;

	public LivenessInterval(String v, int b, int e){
		this.variable = v;
		this.start = b;
		this.stop = e;
	}

	public void setStart(int x){
		this.start = x;
	}

	public int getStart(){
		return this.start;
	}

	public void setStop(int x){
		this.stop = x;
	}

	public int getStop(){
		return this.stop;
	}


}