import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.Appendable;
import java.util.Map;

import cs132.util.IndentPrinter;
import cs132.util.ProblemException;
import cs132.vapor.ast.VBuiltIn.Op;
import cs132.vapor.ast.VDataSegment;
import cs132.vapor.ast.VFunction;
import cs132.vapor.ast.VInstr;
import cs132.vapor.ast.VOperand;
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
		System.out.println("hi");

	}

	public static void convertDataSegments(VDataSegment[] vData) throws IOException {
		for (VDataSegment a : vData) {
			System.out.println("const " + a.ident);
			for(VOperand m : a.values){
				System.out.println(" " + m);
			}
			System.out.println();
		}
	}

	public static void convertFunctions(VFunction[] vf) throws IOException {

		for (VFunction x : vf) {
			FindLiveness fl = new FindLiveness(x);  //create liveness and do analysis
			/*Liveness Analysis Steps:
				First build a CFG, of Nodes for each line in the function, 
				so each node knows about whats next after it and whats before it.
				Then after that it calculates the use and def of each line, 
				so which variable is being used and which variable is being defined.
				Then once we do that we calculate the in and out of each Node or line, 
				so it finds which variable is live coming in and which variables are live going out.
				Then we calcuate the active variable which is just the in variable union defined variables
				Once we know where the variables are active, we go through the nodes and find the start and stop for each variable defined in the function,
				which is just where it starts and stops being active.
				Then it returns a hashmap with the variable name and its liveness interval, so we can just be like 
				li.get("t_0").getStart() or li.get("t_0").getStop()
			*/
			Map<String, LivenessInterval> li = fl.LivenessAnalysis();  
			LinearScan ls = new LinearScan();
			ls.performLinearScan(li); //now this will go and perform linear scan which is in file LinearScan
			int local = 0;
			int in = 0;
			int out = 0;

			String s = "func " + x.ident + " [in " + in + ", out " + out + ", local " + local + "]";
			System.out.println(s);
			TranslateVisitor tp = new TranslateVisitor(ls.getRegisterMap());
			VInstr[] main = x.body;
			for(VInstr i: main){
				i.accept(tp);
			}
			System.out.println();
		}
		
	}

	

	public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException {
		Op[] ops = {
			Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
			Op.PrintIntS, Op.HeapAllocZ, Op.Error,
		};
		boolean allowLocals = true;
		String[] registers = null;
		boolean allowStack = false;
	
		VaporProgram tree;
		try {
			tree = VaporParser.run(new InputStreamReader(in), 1, 1,
														 java.util.Arrays.asList(ops),
														 allowLocals, registers, allowStack);
		}
		catch (ProblemException ex) {
			err.println(ex.getMessage());
			return null;
		}
	
		return tree;
	}

}