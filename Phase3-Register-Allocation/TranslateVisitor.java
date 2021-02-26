import java.io.IOException;

import cs132.vapor.ast.VAssign;
import cs132.vapor.ast.VBranch;
import cs132.vapor.ast.VBuiltIn;
import cs132.vapor.ast.VCall;
import cs132.vapor.ast.VGoto;
import cs132.vapor.ast.VInstr;
import cs132.vapor.ast.VLitInt;
import cs132.vapor.ast.VMemRead;
import cs132.vapor.ast.VMemWrite;
import cs132.vapor.ast.VReturn;

public class TranslateVisitor extends VInstr.Visitor<IOException> {

	public void visit(VAssign a) throws IOException{
		
		System.out.println("hi1");
	}

	public void visit(VBranch b) throws IOException{
		System.out.println("hi2");
	}

	public void visit(VBuiltIn c) throws IOException{

		String reg = getCalleRegister(); //creates a register $t0
		
		System.out.println(reg + " = " + c.op.name + "(" + c.args[0].visit(this)  + ")");
		System.out.println("h3");
	}

	public void visit(VCall c) throws IOException{
		System.out.println("hi4");
	}

	public void visit(VGoto a) throws IOException{
		System.out.println("hi5");
	}

	public void visit(VMemRead b) throws IOException{
		System.out.println("hi6");
	}

	public void visit(VMemWrite c) throws IOException{
		System.out.println("hi7");
	}

	public void visit(VReturn c) throws IOException{
		System.out.println("ret");
	}

	
}