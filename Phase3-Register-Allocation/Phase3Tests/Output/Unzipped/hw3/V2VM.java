import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.Appendable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import RegisterAllocation.*;

import cs132.util.IndentPrinter;
import cs132.util.ProblemException;
import cs132.vapor.ast.VBuiltIn.Op;
import cs132.vapor.ast.VAddr;
import cs132.vapor.ast.VCall;
import cs132.vapor.ast.VCodeLabel;
import cs132.vapor.ast.VDataSegment;
import cs132.vapor.ast.VFunction;
import cs132.vapor.ast.VInstr;
import cs132.vapor.ast.VOperand;
import cs132.vapor.ast.VReturn;
import cs132.vapor.ast.VVarRef;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.parser.VaporParser;

public class V2VM {

	public static void main(String[] args) throws IOException {
		VaporProgram vp = parseVapor(System.in, System.err);
		VDataSegment[] vData = vp.dataSegments;
		// VOperand[] methods = vData[0].values;
		VFunction[] vfunc = vp.functions;
		convertDataSegments(vData);
		convertFunctions(vfunc);

	}

	public static void convertDataSegments(VDataSegment[] vData) throws IOException {
		for (VDataSegment a : vData) {
			System.out.println("const " + a.ident);
			for (VOperand m : a.values) {
				System.out.println(" " + m);
			}
			System.out.println();
		}
	}

	public static void convertFunctions(VFunction[] vf) throws IOException {

		for (VFunction x : vf) {

			FindLiveness fl = new FindLiveness(x); // create liveness and do analysis
			/*
			 * Liveness Analysis Steps: First build a CFG, of Nodes for each line in the
			 * function, so each node knows about whats next after it and whats before it.
			 * Then after that it calculates the use and def of each line, so which variable
			 * is being used and which variable is being defined. Then once we do that we
			 * calculate the in and out of each Node or line, so it finds which variable is
			 * live coming in and which variables are live going out. Then we calcuate the
			 * active variable which is just the in variable union defined variables Once we
			 * know where the variables are active, we go through the nodes and find the
			 * start and stop for each variable defined in the function, which is just where
			 * it starts and stops being active. Then it returns a hashmap with the variable
			 * name and its liveness interval, so we can just be like
			 * li.get("t_0").getStart() or li.get("t_0").getStop()
			 */
			Map<String, LivenessInterval> li = fl.LivenessAnalysis();

			List<String> args = fl.getSavedRegs();
			LinearScan ls = new LinearScan();
			List<String> variables = new ArrayList<>();
			for (String vari : x.vars) {
				variables.add(vari.toString());
			}
			ls.performLinearScan(li, args, variables, x); // now this will go and perform linear scan which is in file
																										// LinearScan

			int local = args.size();
			int in = 0;
			if (x.params.length > 4) {
				in = x.params.length - 4;
			}
			int out = fl.getOut();
			if (out < 0) {
				out = 0;
			}

			String s = "func " + x.ident + " [in " + in + ", out " + out + ", local " + local + "]\n";
			int sizeTotal = 0;
			sizeTotal = local;
			if (sizeTotal > 8) {
				sizeTotal = 8;
			}
			for (int loc = 0; loc < sizeTotal; loc++) {
				s += "  local[" + loc + "] = " + "$s" + loc + "\n";
			}
			Map<String, Register> registers = ls.getRegisterMap();
			List<String> params = new ArrayList<>();
			for (VVarRef v : x.params) {
				if (!args.contains(v.toString())) {
					params.add(v.toString());
				}
			}
			for (int p = 0; p < x.params.length; p++) {
				if (p < 4) {
					if (registers.containsKey(x.params[p].ident)) {
						s += "  " + registers.get(x.params[p].ident).toString() + " = " + "$a" + p + "\n";
					}

				} else {
					int y = p - 4;
					s += "  " + registers.get(params.get(p)).toString() + " = " + "in[" + y + "]\n";
				}
			}

			System.out.print(s);
			HashMap<Integer, String> labels = new HashMap<Integer, String>();
			for (VCodeLabel lab : x.labels) {
				if (labels.containsKey(lab.instrIndex)) {
					String labelString = labels.get(lab.instrIndex) + ":\n";
					labelString += lab.ident;
					labels.put(lab.instrIndex, labelString);
				} else {
					labels.put(lab.instrIndex, lab.ident);
				}
			}
			TranslateVisitor tp = new TranslateVisitor(registers, labels, local);
			VInstr[] main = x.body;
			int index = 1;
			if (labels.containsKey(0)) {
				tp.printLabel(0);
			}
			for (VInstr i : main) {
				i.accept(tp);
				tp.printLabel(index);
				index++;
			}
			System.out.println();
		}

	}

	public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException {

		Op[] ops = { Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS, Op.PrintIntS, Op.HeapAllocZ, Op.Error, };
		boolean allowLocals = true;
		String[] registers = null;
		boolean allowStack = false;

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