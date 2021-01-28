package typechecker;
import java.util.HashMap;
public class MethodSymbol{
	HashMap<String, Symbol> fields = new HashMap<String, Symbol>();
	HashMap<String, MethodSymbol> methods = new HashMap<String, MethodSymbol>();
}