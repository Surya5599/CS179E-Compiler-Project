package typechecker;
import java.util.HashMap;
public class ClassSymbol{
	HashMap<String, Symbol> fields = new HashMap<String, Symbol>();
	HashMap<String, MethodSymbol> methods = new HashMap<String, MethodSymbol>();
}