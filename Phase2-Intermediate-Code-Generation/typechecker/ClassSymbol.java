package typechecker;

import java.util.*;

public class ClassSymbol {
	private Symbol name;
	private HashMap<Symbol, String> fields;
	private HashMap<String, MethodSymbol> methods;
	private List<String> vTable;

	public ClassSymbol(String n) {
		name = Symbol.symbol(n);
		fields = new HashMap<Symbol, String>();
		methods = new HashMap<String, MethodSymbol>();
		this.vTable = new ArrayList<String>();
	}

	public String getClassId() {
		return this.name.toString();
	}

	public void addMethod(String name, String type) {
		this.methods.put(name, new MethodSymbol(name, type));
		vTable.add(name);
	}

	public List<String> getvTable(){
		return vTable;
	}

	public void addFields(String name, String type) {
		this.fields.put(Symbol.symbol(name), type);
	}

	public MethodSymbol getMethod(String name) {
		return this.methods.get(name);
	}

	public String getField(String name) {
		return this.fields.get(Symbol.symbol(name));
	}

	public int methodSize() {
		return methods.size();
	}

	public Set<String> getMethodNames() {
		return methods.keySet();
	}

}