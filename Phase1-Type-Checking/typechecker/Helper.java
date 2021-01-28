package typechecker;

import java.util.HashSet;
import java.util.Set;

import syntaxtree.*;

public class Helper {
	public static boolean classDistinct(NodeList names) {
		Set<String> distinct = new HashSet<>();
		for (int j = 0; j < names.size(); j++) {
			String id = Helper.className(names.elementAt(j));
			if (distinct.contains(id)) {
				return false;
			} else {
				distinct.add(id);
			}
		}
		return true;
	}

	public static String className(Node cname) { // helper function className
		String nameOfClass = "";
		if (cname instanceof MainClass) {
			nameOfClass = ((MainClass) cname).f1.f0.toString();
		} else if (cname instanceof ClassDeclaration) {
			nameOfClass = ((ClassDeclaration) cname).f1.f0.toString();
		} else if (cname instanceof ClassExtendsDeclaration) {
			nameOfClass = ((ClassExtendsDeclaration) cname).f1.f0.toString();
		}
		return nameOfClass;
	}

	// public static MethodDistinct(NodeList names){
	// 	{MethodDeclaration, MethodDeclaration, MethodDeclaration, MethodDeclaration, MethodDeclaration}
	// }

	public static String methodName(MethodDeclaration method) {// helper function methodName return
		return method.f2.f0.toString();
	}

	public static String methodType(MethodDeclaration method) {// helper function methodName return
		return getIntegerType((IntegerType) method.f1.f0.choice);
	}

	public static String getIntegerType(IntegerType integer) {// helper function methodName return
		return integer.f0.toString();
	}

	public static String getId(Identifier id) {// helper function methodName return
		return id.f0.toString();
	}

	public static String getType(Type t) {// helper function methodName return
		Node x = t.f0.choice;
		String typename = "";
		if (x instanceof IntegerType) {
			typename = ((IntegerType) x).f0.toString();
		} else if (x instanceof BooleanType) {
			typename = ((BooleanType) x).f0.toString();
		}
		return typename;
	}

}
