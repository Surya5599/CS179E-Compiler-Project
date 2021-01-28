package typechecker;

import java.util.HashMap;

public class ClassSymbol {
	private Symbol name;
	private HashMap<String, Symbol> fields;
	private HashMap<String, MethodSymbol> methods;

	public ClassSymbol(String n) {
		name = Symbol.symbol(n);
		fields = new HashMap<String, Symbol>();
		methods = new HashMap<String, MethodSymbol>();
	}

	public String getClassId() {
		return this.name.toString();
	}

	public void addMethod(String name, String type) {
		this.methods.put(name, new MethodSymbol(name, type));
	}

	public MethodSymbol getMethod(String name) {
		return this.methods.get(name);
	}

	@Override
	public String toString(){
		return null;
	}

}