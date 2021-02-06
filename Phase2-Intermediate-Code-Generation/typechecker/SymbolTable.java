package typechecker;

import java.util.*;

public class SymbolTable {
	private HashMap<String, ClassSymbol> classes;
	private List<String> classNames;


	public SymbolTable() {
		classes = new HashMap<String, ClassSymbol>();
		this.classNames = new ArrayList<String>();
	}

	public void addClass(String x) {
		ClassSymbol c = new ClassSymbol(x);
		classes.put(x, c);
		classNames.add(x);
	}

	public ClassSymbol getClass(String x) {
		return classes.get(x);
	}

	public List<String> getClassList(){
		return classNames;
	}
}
