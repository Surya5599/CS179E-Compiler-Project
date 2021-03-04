import java.io.IOException;

import cs132.vapor.ast.VAddr;
import cs132.vapor.ast.VAssign;
import cs132.vapor.ast.VBranch;
import cs132.vapor.ast.VBuiltIn;
import cs132.vapor.ast.VCall;
import cs132.vapor.ast.VGoto;
import cs132.vapor.ast.VInstr;
import cs132.vapor.ast.VLabelRef;
import cs132.vapor.ast.VLitInt;
import cs132.vapor.ast.VMemRead;
import cs132.vapor.ast.VMemRef;
import cs132.vapor.ast.VMemWrite;
import cs132.vapor.ast.VOperand;
import cs132.vapor.ast.VReturn;
import cs132.vapor.ast.VVarRef;

public class TranslateVisitor extends VInstr.Visitor<IOException> {

	Printer p;

	public TranslateVisitor(Printer p) {
		this.p = p;
	}

	@Override
	public void visit(VAssign v) throws IOException {
		String s = "";
		if (v.dest instanceof VVarRef.Local) {
			s += v.dest.toString();
		} else if (v.dest instanceof VVarRef.Register) {
			s += v.dest.toString();
		}
		if (v.source instanceof VLitInt) {
			s += " " + v.source.toString();
			s = "li " + s;
		} else {
			s += " " + v.source.toString();
			s = "move " + s;
		}

		p.print(s);
	}

	@Override
	public void visit(VCall v) throws IOException {
		p.print("jalr " + v.addr.toString());

	}

	@Override
	public void visit(VBuiltIn v) throws IOException {
		if (v.op != null) {
			String call = "";
			if (v.op.name == "HeapAllocZ") {
				for (VOperand vO : v.args) {
					if (vO instanceof VLitInt) {
						p.print("li $a0 " + vO.toString());
					}
					if (vO instanceof VVarRef) {
						p.print("move $a0 " + vO.toString());
					}
				}
				call = "_heapAlloc";
				p.print("jal " + call);
			} else if (v.op.name == "Error") {
				for (VOperand vO : v.args) {
					if (vO instanceof VLitInt) {
						p.print("li $a0 " + vO.toString());
					}
					if (vO instanceof VVarRef) {
						p.print("move $a0 " + vO.toString());
					}
				}
				call = "_error";
				p.print("j " + call);
			} else if (v.op.name == "PrintIntS") {
				for (VOperand vO : v.args) {
					if (vO instanceof VLitInt) {
						p.print("li $a0 " + vO.toString());
					}
					if (vO instanceof VVarRef) {
						p.print("move $a0 " + vO.toString());
					}
				}
				call = "_print";
				p.print("jal " + call);
			} else if (v.op.name == "LtS") {
				String first = v.dest.toString();
				String second = v.args[0].toString();
				String third = v.args[1].toString();
				p.print("slti " + first + " " + second + " " + third);
				return;
			}
		}

		if (v.dest != null) {
			p.print("move " + v.dest.toString() + " $v0");
		}

	}

	@Override
	public void visit(VMemWrite v) throws IOException {

		if (v.source != null) {
			if (v.source instanceof VLabelRef) {
				p.print("la $t9 " + ((VLabelRef) v.source).ident);
			} else if (v.source instanceof VVarRef.Register) {
				String s;
				if (v.dest instanceof VMemRef.Stack) {
					p.print("sw " + v.source.toString() + " " + ((VMemRef.Stack) v.dest).index + "($sp)");
				}

			}
		}
		if (v.dest != null) {
			if (v.dest instanceof VMemRef.Global) {
				VMemRef.Global vmem = (VMemRef.Global) v.dest;
				VAddr vadd = (VAddr) vmem.base;
				p.print("sw $t9 0(" + vadd.toString() + ")");
			}

		}

	}

	@Override
	public void visit(VMemRead v) throws IOException {
		// TODO Auto-generated method stub
		if (v.source instanceof VMemRef.Global) {
			VMemRef.Global vmem = (VMemRef.Global) v.source;
			VAddr vadd = (VAddr) vmem.base;
			p.print("lw " + v.dest.toString() + " 0(" + vadd.toString() + ")");
		}

	}

	@Override
	public void visit(VBranch v) throws IOException {
		if (v.positive == true) {
			p.print("bnez " + v.value.toString() + " " + ((VLabelRef) v.target).ident);
			p.print("la $a0 _str0");
		} else {

		}

	}

	@Override
	public void visit(VGoto v) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(VReturn v) throws IOException {
		// TODO Auto-generated method stub

	}

}