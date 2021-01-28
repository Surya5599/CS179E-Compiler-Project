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

	/*
	 * public string methodName(Node method) {// helper function methodName return
	 * ((MethodDeclaration)method).f2.f0.toString(); }
	 */
}
