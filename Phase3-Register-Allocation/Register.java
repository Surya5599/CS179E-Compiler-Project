import java.util.ArrayList;
import java.util.List;

public class Register {

		public static Register t0 = new Register("t0");
		public static Register t1 = new Register("t1");
		public static Register t2 = new Register("t2");
		public static Register t3 = new Register("t3");
		public static Register t4 = new Register("t4");
		public static Register t5 = new Register("t5");
		public static Register t6 = new Register("t6");
		public static Register t7 = new Register("t7");
		public static Register t8 = new Register("t8");

		public static Register s0 = new Register("s0");
		public static Register s1 = new Register("s1");
		public static Register s2 = new Register("s2");
		public static Register s3 = new Register("s3");
		public static Register s4 = new Register("s4");
		public static Register s5 = new Register("s5");
		public static Register s6 = new Register("s6");
		public static Register s7 = new Register("s7");

		public static Register a0 = new Register("a0");
		public static Register a1 = new Register("a1");
		public static Register a2 = new Register("a2");
		public static Register a3 = new Register("a3");

		public static Register v0 = new Register("v0");
		public static Register v1 = new Register("v1");

		private List<Register> caller_saved;
		private List<Register> callee_saved;
		private List<Register> argument_passing;


		private String name; 
		public Register(String s) {
			this.name = "$"+ s;
		}

		public String toString(){
			return this.name;
		}

		public List<Register> getCallerSaved(){
			return this.caller_saved;
		}

		public List<Register> getCalleeSaved(){
			return this.callee_saved;
		}




		public Register(){
			caller_saved = new ArrayList<Register>();
			callee_saved = new ArrayList<Register>();
			argument_passing = new ArrayList<Register>();

			caller_saved.add(t0);
			caller_saved.add(t1);
			caller_saved.add(t2);
			caller_saved.add(t3);
			caller_saved.add(t4);
			caller_saved.add(t5);
			caller_saved.add(t6);
			caller_saved.add(t7);
			caller_saved.add(t8);

			callee_saved.add(s0);
			callee_saved.add(s1);
			callee_saved.add(s2);
			callee_saved.add(s3);
			callee_saved.add(s4);
			callee_saved.add(s5);
			callee_saved.add(s6);
			callee_saved.add(s7);

			argument_passing.add(a0);
			argument_passing.add(a1);
			argument_passing.add(a2);
			argument_passing.add(a3);
		}
}