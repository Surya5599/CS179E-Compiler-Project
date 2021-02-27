import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VDataSegment;
import cs132.vapor.ast.VFunction;
import cs132.vapor.ast.VInstr;
import cs132.vapor.ast.VOperand;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.VBuiltIn.Op;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;

public class V2VM {


	public static void main(String[] args) throws IOException {
		VaporProgram vp = parseVapor(System.in, System.err);
		VDataSegment[] vData = vp.dataSegments;
		VFunction[] vfunc = vp.functions;
		TranslateVisitor tp = new TranslateVisitor();
		convertDataSegments(vData);
		convertFunctions(vfunc);
		System.out.println("hi");
		

	}

	public static void convertDataSegments(VDataSegment[] vData) throws IOException{
		for(VDataSegment a : vData){
			System.out.println(a.ident);
			for(VOperand m : a.values){
				System.out.println(" " + m);
			}
		}
	}

	public static void convertFunctions(VFunction[] vf) throws IOException{
		for(VFunction x: vf){
			System.out.println(x);
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