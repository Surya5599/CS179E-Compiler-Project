package typechecker;

import java.util.HashMap;

public class SymbolTable {
	private HashMap<String, ClassSymbol> classes;

	public SymbolTable() {
		classes = new HashMap<String, ClassSymbol>();
	}

	public void addClass(String x) {
		ClassSymbol c = new ClassSymbol(x);
		classes.put(x, c);
	}

	public ClassSymbol getClass(String x) {
		return classes.get(x);
	}
}
