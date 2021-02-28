import java.io.IOException;
import java.util.List;
import java.util.Map;

import cs132.vapor.ast.VAddr;
import cs132.vapor.ast.VAssign;
import cs132.vapor.ast.VBranch;
import cs132.vapor.ast.VBuiltIn;
import cs132.vapor.ast.VCall;
import cs132.vapor.ast.VGoto;
import cs132.vapor.ast.VInstr;
import cs132.vapor.ast.VLitInt;
import cs132.vapor.ast.VMemRead;
import cs132.vapor.ast.VMemRef;
import cs132.vapor.ast.VMemWrite;
import cs132.vapor.ast.VOperand;
import cs132.vapor.ast.VReturn;
import cs132.vapor.ast.VVarRef;

public class TranslateVisitor extends VInstr.Visitor<IOException> {
	Map<String, Register> register;
	private int indent;
	Map<Integer, String> labels;

	public TranslateVisitor(Map<String, Register> reg, Map<Integer, String> labels2) {
		this.register = reg;
		this.indent = 1;
		this.labels = labels2;
	}

	public void printLabel(int x){
		if(labels.containsKey(x)){
			System.out.println(labels.get(x) + ":");
		}
	}

	public void visit(VAssign a) throws IOException {
		String s = register.get(a.dest.toString()).toString() + " = " + a.source.toString();
		System.out.println(printIndent() + s);
	}

	public void visit(VBranch b) throws IOException {
		String s = "if " + register.get(b.value.toString()).toString() + " goto ";
		if(b.target.toString() != null) {
			s += b.target.toString();
		}

		System.out.println(printIndent() + s);
	}

	public void visit(VBuiltIn c) throws IOException {
		String s = "";
		String arguments = "";
		for (VOperand o : c.args) {
			if(o instanceof VVarRef){
				arguments += register.get(o.toString()).toString() + " "; 
			}
			else{
				arguments += o.toString() + " ";
			}
		}
		arguments = arguments.trim();
		if(c.dest != null){
			s = register.get(c.dest.toString()).toString() + " = " + c.op.name  + "(" + arguments  + ")" ;
		}
		else{
			s = c.op.name  + "(" + arguments  + ")";
		}
		System.out.println(printIndent() + s);
	}

	private String printIndent(){
		int spaces = indent * 2;
		return String.format("%" + spaces + "s", ""); 
	}

	public void visit(VCall c) throws IOException{
		String s = "VCall";
		int i = 0;
		for (VOperand a : c.args){
			if(i < 4){
				if(a instanceof VVarRef){
					System.out.println(printIndent() + "$a" + i + " = " + register.get(a.toString()).toString()); 
				}
				else{
					System.out.println(printIndent() + "$a" + i + " = " + a.toString());
				}
			}
			else{
				if(a instanceof VVarRef){
					System.out.println(printIndent() + "out[" + i + "] = " + register.get(a.toString()).toString()); 
				}
				else{
					System.out.println(printIndent() + "out[" + i + "] = " + a.toString());
				}
			}
			i++;
		}
		if(c.dest != null){
			s = register.get(c.dest.toString()).toString();
		}
		System.out.println(printIndent() + "call " + s); //?
	}

    
	public void visit(VGoto a) throws IOException{
		String s = a.target.toString();
		System.out.println(printIndent() + "goto " +  s);
	}

	public void visit(VMemRead b) throws IOException{
			VMemRef.Global vmem = (VMemRef.Global)b.source;
		String s = register.get(b.dest.toString()).toString() + " = [" + register.get(vmem.base.toString()).toString() + "]";
		//String s = "VMemRead";
		System.out.println(printIndent() + s);
	}

	public void visit(VMemWrite c) throws IOException{
		String s = "";
		if(c.dest instanceof VMemRef.Global){
			//VMemRef = (VMemRef.Global)c.dest;
			VMemRef.Global vmem = (VMemRef.Global)c.dest;
			s = "[" + register.get(vmem.base.toString()).toString() + "] = " + c.source.toString();
		}
		else{
			s = register.get(c.dest.toString()).toString() + " = " + c.source.toString();
		}

		System.out.println(printIndent() + s);
	}

	public void visit(VReturn c) throws IOException{
		String s = "ret";
		if(c.value != null){
			s = c.value.toString();
			System.out.println(printIndent() + "$v0 = " + register.get(s).toString());
	
		}
		System.out.println(printIndent() + "ret");

	}

	
}