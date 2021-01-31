package typechecker;

import java.util.HashMap;

public class ClassSymbol {
	private Symbol name;
	private HashMap<Symbol, String> fields;
	private HashMap<String, MethodSymbol> methods;

	public ClassSymbol(String n) {
		name = Symbol.symbol(n);
		fields = new HashMap<Symbol, String>();
		methods = new HashMap<String, MethodSymbol>();
	}

	public String getClassId() {
		return this.name.toString();
	}

	public void addMethod(String name, String type) {
		this.methods.put(name, new MethodSymbol(name, type));
	}

	public void addFields(String name, String type){
		this.fields.put(Symbol.symbol(name), type);
	}

	public MethodSymbol getMethod(String name) {
		return this.methods.get(name);
	}

	public String getField(String name){
		return this.fields.get(Symbol.symbol(name));
	}

}