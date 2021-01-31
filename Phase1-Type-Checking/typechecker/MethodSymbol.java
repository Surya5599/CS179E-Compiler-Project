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

	public String getMethodType(){
		return this.method_type;
	}

	public String getLocalType(String x){
		return this.locals.get(Symbol.symbol(x));
	}

	public void addLocal(String name, String type) {
		this.locals.put(Symbol.symbol(name), type);
	}

	public void addParam(String name, String type) {
		this.params.put(Symbol.symbol(name), type);
	}

	public String getParamType(String x) {
		return this.params.get(Symbol.symbol(x));
	}

	public int paramSize(){
		return this.params.size();
	}
}