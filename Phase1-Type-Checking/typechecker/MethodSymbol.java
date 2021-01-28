package typechecker;

import java.util.HashMap;

public class MethodSymbol {
	private String method_name;
	private String method_type;
	private HashMap<Symbol, String> locals;
	private HashMap<Symbol, String> params;

	public MethodSymbol(String name, String type) {
		method_name = name;
		method_type = type;
		locals = new HashMap<Symbol, String>();
		params = new HashMap<Symbol, String>();
	}

	public String getMethodId() {
		return this.method_name;
	}

	public void addLocal(String name, String type) {
		this.locals.put(Symbol.symbol(name), type);
	}

	public void addParam(String name, String type) {
		this.params.put(Symbol.symbol(name), type);
	}
}