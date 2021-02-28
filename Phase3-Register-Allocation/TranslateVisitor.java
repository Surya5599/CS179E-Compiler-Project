import java.io.IOException;
import java.util.Map;

import cs132.vapor.ast.VAssign;
import cs132.vapor.ast.VBranch;
import cs132.vapor.ast.VBuiltIn;
import cs132.vapor.ast.VCall;
import cs132.vapor.ast.VGoto;
import cs132.vapor.ast.VInstr;
import cs132.vapor.ast.VLitInt;
import cs132.vapor.ast.VMemRead;
import cs132.vapor.ast.VMemWrite;
import cs132.vapor.ast.VOperand;
import cs132.vapor.ast.VReturn;
import cs132.vapor.ast.VVarRef;

public class TranslateVisitor extends VInstr.Visitor<IOException> {
	Map<String, Register> register;
	private int indent;

	public TranslateVisitor(Map<String, Register> reg) {
		this.register = reg;
		this.indent = 1;
	}

	public void visit(VAssign a) throws IOException {
		String s = register.get(a.dest.toString()).toString() + " = " + a.source.toString();
		System.out.println(printIndent() + s);
	}

	public void visit(VBranch b) throws IOException {
		String s = "VBranch";
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
		return String.format("%" + indent + "s", ""); 
	}

	public void visit(VCall c) throws IOException{
		String s = "VCall";
		System.out.println(printIndent() + s);
	}


	public void visit(VGoto a) throws IOException{
		String s = "VGoto";
		System.out.println(printIndent() + s);
	}

	public void visit(VMemRead b) throws IOException{
		String s = "VMemRead";
		System.out.println(printIndent() + s);
	}

	public void visit(VMemWrite c) throws IOException{
		String s = "VMemWrite";
		System.out.println(printIndent() + s);
	}

	public void visit(VReturn c) throws IOException{
		String s = "ret";
		System.out.println(printIndent() + s);
	}

	
}