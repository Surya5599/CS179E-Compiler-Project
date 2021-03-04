import cs132.util.IndentPrinter;
import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VAddr;
import cs132.vapor.ast.VCodeLabel;
import cs132.vapor.ast.VDataSegment;
import cs132.vapor.ast.VFunction;
import cs132.vapor.ast.VInstr;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.VBuiltIn.Op;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class VM2M {

	public static void main(String[] args) throws IOException {
		VaporProgram vp = parseVapor(System.in, System.err);
		Printer printer = new Printer();
		TranslateDataSegments(vp.dataSegments, printer);
		TranslateFunction(vp.functions, printer);
		PrintLastThings(printer);

	}

	private static void PrintLastThings(Printer p) {
		p.print("_print:");
		p.indent();
		p.print("li $v0 1   # syscall: print integer");
		p.print("syscall");
		p.print("la $a0 _newline");
		p.print("li $v0 4   # syscall: print string");
		p.print("syscall");
		p.print("jr $ra");
		p.dedent();
		p.newLine();
		p.print("_error:");
		p.indent();
		p.print("li $v0 4   # syscall: print string");
		p.print("syscall");
		p.print("li $v0 10  # syscall: exit");
		p.print("syscall");
		p.newLine();
		p.dedent();
		p.print("_heapAlloc:");
		p.indent();
		p.print("li $v0 9   # syscall: sbrk");
		p.print("syscall");
		p.print("jr $ra");
		p.dedent();
		p.newLine();
		p.print(".data");
		p.print(".align 0");
		p.print("_newline: .asciiz \"\\n\"");
		p.print("_str0: .asciiz \"null pointer\\n\"");

	}

	private static void TranslateDataSegments(VDataSegment[] dataSegments, Printer p) {
		// to indent do p.indent();
		// to dedent do p.dedent();
		// to print do p.print(String);
		// to print new line p.newLine()

		// see samples in Phase5Test
		// the .data
		// function names

		// the .text
		// i think its the same thing over and over

		// make VM2M to run the thing

	}

	private static int findStack(VFunction func) {
		int stackFrame = 0;
		stackFrame = (2 + func.stack.out + func.stack.local) * 4;
		return stackFrame;
	}

	private static void TranslateFunction(VFunction[] functions, Printer p) throws IOException {
		for (VFunction func : functions) {
			p.print(func.ident + ":");
			p.indent();
			p.print("sw $fp -8($sp)");
			p.print("move $fp $sp");
			p.print("subu $sp $sp " + findStack(func));
			p.print("sw $ra -4($fp)");
			int instrNum = 1;
			for (VInstr inst : func.body) {
				TranslateVisitor tv = new TranslateVisitor(p);
				inst.accept(tv);
				for (VCodeLabel vCode : func.labels) {
					if (vCode.instrIndex == instrNum) {
						p.dedent();
						p.print(vCode.ident + ":");
						p.indent();
					}
				}
				instrNum++;
			}
			p.print("lw $ra -4($fp)");
			p.print("lw $fp -8($fp)");
			p.print("addu $sp $sp " + findStack(func));
			p.print("jr $ra\n");
			p.dedent();
		}
	}

	private static void print(String s) {
		System.out.println(s);
	}

	public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException {
		Op[] ops = { Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS, Op.PrintIntS, Op.HeapAllocZ, Op.Error, };

		boolean allowLocals = false;
		String[] registers = { "v0", "v1", "a0", "a1", "a2", "a3", "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "s0",
				"s1", "s2", "s3", "s4", "s5", "s6", "s7", "t8", };
		boolean allowStack = true;

		VaporProgram tree;
		try {
			tree = VaporParser.run(new InputStreamReader(in), 1, 1, java.util.Arrays.asList(ops), allowLocals, registers,
					allowStack);
		} catch (ProblemException ex) {
			err.println(ex.getMessage());
			return null;
		}

		return tree;
	}

}