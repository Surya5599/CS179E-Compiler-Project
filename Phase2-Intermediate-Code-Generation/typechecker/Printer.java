package typechecker;

import java.util.List;

public class Printer {
	private int depth;
	private int variable;

	public Printer(){
		this.depth = 0;
		this.variable = 0;
	}

	public void main(){
		print("func Main()");
		this.depth++;
	}

	public void addEnter(){
		print("");
	}

	public void classes(String cName, List<String> list){
		print("const vmt_" + cName);
		this.depth++;
		for(int i = 0; i < list.size(); i++){
			print(":"+cName+"."+list.get(i));
		}
		this.depth--;
	}

	public void output(String s){
		print("PrintIntS(" + s + ")");
	}

	public String add(String x, String y){
		String var = "var." + variable;
		print(var + " = Add(" + x + " " + y + ")");
		return var;
	}

	public String subtract(String x, String y){
		String var = "var." + variable;
		print(var + " = Sub(" + x + " " + y + ")");
		return var;
	}

	public String mult(String x, String y){
		String var = "var." + variable;
		print(var + " = MultS(" + x + " " + y + ")");
		return var;
	}

	public void ret(){
		print("ret");
		this.depth--;
	}

	public void function(String name, List<String> args){
		print(name);
	}

	private void print(String s){
		if(depth > 0){
			String space = String.format("%"+ depth +"s", " ");
			System.out.print(space);
		}
			System.out.println(s);
	}
}
