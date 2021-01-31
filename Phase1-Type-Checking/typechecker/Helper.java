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

	public static boolean MethodDistinct(NodeListOptional names){
		Set<String> distinct = new HashSet<>();
		
		for(int j = 0; j < names.size(); j++){
			String methodName = Helper.methodName((MethodDeclaration) names.elementAt(j));
			if(distinct.contains(methodName)){
				return false;
			}
			else{
				distinct.add(methodName);
			}
		}
		return true;
	}

	public static boolean idDistinct(NodeListOptional ids){
		Set<String> distinct = new HashSet<>();
		
		for(int j = 0; j < ids.size(); j++){
			String methodName = Helper.getId(((VarDeclaration) ids.elementAt(j)).f1);
			if(distinct.contains(methodName)){
				return false;
			}
			else{
				distinct.add(methodName);
			}
		}
		return true;
	}


	public static boolean parameterDistinct(FormalParameterList params){
		FormalParameter fp = params.f0; //there will def be one parameter
		NodeListOptional nlo = params.f1; // more than one is optional therefore we have a nodeListoptional..ithink

		Set<String> distinct = new HashSet<>();
		distinct.add(getId(fp.f1)); //fp.f1 is an identifer

		for(int i = 0; i < nlo.size(); i++){
			String paraName = Helper.getId(((FormalParameterRest) nlo.elementAt(i)).f1.f1); 
			if(distinct.contains(paraName)){
				return false;
			}
			else{
				distinct.add(paraName);
			}
		}	
		return true;

	}

	public static String methodName(MethodDeclaration method) {// helper function methodName return
		return method.f2.f0.toString();
	}

	public static String methodType(MethodDeclaration method) {// helper function methodName return
		return getType(method.f1);
	}

	public static String getIntegerType(IntegerType integer) {// helper function methodName return
		return integer.f0.toString();
	}

	public static String getId(Identifier id) {// helper function methodName return
		return id.f0.toString();
	}

	public static int getListSize(Node n){
		return ((ExpressionList)n).f1.size() + 1;
	}

	public static String getType(Type t) {// helper function methodName return
		Node x = t.f0.choice;
		String typename = "";
		if (x instanceof IntegerType) {
			typename = ((IntegerType) x).f0.toString();
		} else if (x instanceof BooleanType) {
			typename = ((BooleanType) x).f0.toString();
		}
		else if(x instanceof ArrayType){
			typename = "int []";
		}
		else if (x instanceof Identifier){
			typename = getId((Identifier)x);
		}
		return typename;
	}

}
