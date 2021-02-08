package typechecker;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Printer {
	private int depth;
	private int variable;
	private int label;
	private int nullLabel;
	private int ifElse;
	private int whileCount;
	private HashMap<String, String> varInfo;

	public String getClassName(String s){
		return varInfo.get(s);
		
	}

	public Printer(){
		this.depth = 0;
		this.label = 0;
		this.variable = 0;
		this.nullLabel = 0;
		this.ifElse = 0;
		this.whileCount = 0;
		this.varInfo = new HashMap<String, String>();
	}

	public String createLabel(){
		label++;
		return "Label"+label;
	}

	public void main(){
		print("func Main()");
		this.depth++;
	}

	public void addEnter(){
		print("");
	}

	public void classes(String cName, Set<String> list){
		print("const vmt_" + cName);
		this.depth++;
		for(String s : list){
			print(":"+cName+"."+ s);
		}
		this.depth--;
		addEnter();
	}

	public void output(String s){
		print("PrintIntS(" + s + ")");
	}

	public String getRecord(Integer x){
		String var = createVar();
		print(var + " = [this+" + x + "]");
		return var;
	}

	public String createVar(){
		String var = "t." + variable;
		variable++;
		return var;
	}

	public String add(String x, String y){
		String var = createVar();
		print(var + " = Add(" + x + " " + y + ")");
		return var;
	}

	public String subtract(String x, String y){
		String var = createVar();
		print(var + " = Sub(" + x + " " + y + ")");
		return var;
	}

	public String mult(String x, String y){
		String var = createVar();
		print(var + " = MulS(" + x + " " + y + ")");
		return var;
	}

	public String eq(String x, String y){
		String var = createVar();
		print(var + " = Eq(" + x + " " + y + ")");
		return var;
	}

	public String lt(String x, String y){
		String var = createVar();
		print(var + " = Lt(" + x + " " + y + ")");
		return var;
	}

	public String beginWhile(){
		String cWhile = "while"+whileCount;
		whileCount++;
		print(cWhile + "_top:");
		return cWhile;
	}

	public void continueWhile(String whileLabel, String varLabel){
		print("if0 " + varLabel +  " goto :" + whileLabel + "_end");
		depth++;
	}

	public void endWhile(String someLabel){
		print("goto :" + someLabel + "_top");
		depth--;
		print(someLabel + "_end:");
	}

	public String beginSS(String varLabel){
		String s = "ss" + label;
		label++;
		print("if0 " + varLabel + " goto :" + s + "_else");
		depth++;
		return s;
	}

	public void continueSS(String l){
		label++;
		print("goto :" +l + "_end");
		depth--;
  	print(l+"_else:");
		depth++;
	}

	public void noWork(String l){
		print(l + " = 0");
	}

	public void endSS(String someLabel){
		depth--;
		print(someLabel + "_end:");
	}

	public String lts(String x, String y){
		String var = createVar();
		print(var + " = LtS(" + x + " " + y + ")");
		return var;
	}

	public String arrayPrint(String name, String loc){
		String str = name;
		if(name.contains("this+")){
			String v = createVar();
			print(v + " = " + name);
			name = v;
		}
		print("s = [" + name + "]");
		print("ok = LtS(" + loc + " s)");
		String l1 = createLabel();
		print("if ok goto :" + l1);
		print("Error(\"array index out of bounds\")");
		print(l1 + ": ok = LtS(-1 " + loc + ")");
		String l2 = createLabel();
		print("if ok goto :" + l2);
		print("Error(\"array index out of bounds\")");
		print(l2 + ": o =  MulS(" + loc + " 4)");
		if(str.contains("this+")){
			String v = createVar();
			print(v + " = " + str);
			str = v;
		}
		String d = createVar();
		print(d + " = Add("+str+" o)");
		return d;
	}

	public String lookup(String name, String loc){
		String str = name;
		if(name.contains("this+")){
			String v = createVar();
			print(v + " = " + name);
			name = v;
		}
		print("s = [" + name + "]");
		print("ok = Lt(" + loc + " s)");
		String l1 = createLabel();
		print("if ok goto :" + l1);
		print("Error(\"array index out of bounds\")");
		print(l1 + ": o =  MulS(" + loc + " 4)");
		if(str.contains("this+")){
			String v = createVar();
			print(v + " = " + str);
			str = v;
		}
		print("d = Add("+str+" o)");
		String r = createVar();
		print(r + " = [d+4]");
		return r;
	}

	public String callFunc(String s){
		String var = createVar();
		print(var + " = " + s);
		return var;
	}

	public void newArray(){
		print("func AllocArray(size)");
		this.depth++;
		print("bytes = MulS(size 4)");
		print("bytes = Add(bytes 4)");
		print("v = HeapAllocZ(bytes)");
		print("[v] = size");
		print("ret v");
		this.depth--;
	}

	public void ret(String s){
		if(s == null){
			s = "";
		}
		print("ret " + s);
		this.depth--;
	}

	public void assign(String var, String equals){
		print(var + " = " + equals);
	}

	public void methodDeclare(String cName, String mName, String params){
		variable = 0;
		if(params == null){
			params = "";
		}
		else{
			params = " " + params;
		}
		params = "this" + params;
		print("func " + cName + "." + mName +  "(" + params  + ")");
		this.depth++;
	}

	public String allocation(String cname, int size){
		size = size * 4 + 4;
		String var = createVar();
		varInfo.put(var, cname);
		print(var + " = HeapAllocZ(" + size + ")");
		print("[" + var + "] = :vmt_" + cname);
		// print("if " + var + " goto :null" + label);
		// this.depth++;
		// print("Error(\"null pointer\")");
		// this.depth--;
		// print("null" + label + ":");
		// label++;
		return var;
	}

	public String functionCall(String cVar, int offset, String param){
		if(cVar.contains("this+")){
			String v = createVar();
			print(v + " = " + cVar);
			cVar = v;
		}
		print("if " + cVar + " goto :null" + nullLabel);
		this.depth++;
		print("Error(\"null pointer\")");
		this.depth--;
		print("null" + nullLabel + ":");
		nullLabel++;
		String var = createVar();
		print(var + " = [" + cVar + "]");
		print(var + " = [" + var + "+" + offset + "]");
		String newVar = createVar();
		if(param == null){
			param = "";
		}
		else{
			param = " " + param;
		}
		print(newVar +  " = call " + var + "(" + cVar + param + ")");
		return newVar;
	}

	public String beginIf(String s){
		String if0 = "if0";
		String ifLabel = "if" + ifElse;
		ifElse++;
		print(if0 + " " + s + " goto :" + ifLabel + "_else");
		this.depth++;
		return ifLabel;
	}

	public void beginIfElse(String ifLabel){
		print(ifLabel +"_else:");
		this.depth++;
	}

	public void endIfElse(String ifLabel){
		this.depth--;
		print(ifLabel+"_end:");
	}

	public void endIf(String ifLabel){

		print("goto :" + ifLabel+"_end");
		this.depth--;
	}

	private void print(String s){
		if(depth > 0){
			String space = String.format("%"+ depth +"s", " ");
			System.out.print(space);
		}
			System.out.println(s);
	}

	
}
