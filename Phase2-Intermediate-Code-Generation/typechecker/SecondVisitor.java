package typechecker;

import syntaxtree.*;
import visitor.*;

public class SecondVisitor extends GJDepthFirst<String, SymbolTable> {
	private SymbolTable st;
	private ClassSymbol currClass;
	private MethodSymbol currMethod;
	private boolean anyErrors = false;

	public boolean getErrors(){
		return anyErrors;
	}

	public SecondVisitor(SymbolTable stable) {
		this.st = stable;
	}

	public String visit(Goal n, SymbolTable stable){
		n.f0.accept(this, stable);
		n.f1.accept(this, stable);
		return null;
	}

	public String visit(MainClass n, SymbolTable stable) {
		currClass = st.getClass(Helper.className(n));
		currMethod = currClass.getMethod("main");
		n.f15.accept(this, stable);
		return null;
	}

	public String visit(ClassDeclaration n, SymbolTable stable){
		currClass = st.getClass(Helper.className(n));
		currMethod = null;
		n.f4.accept(this, stable);
		return null;
	}

	public String visit(MethodDeclaration n, SymbolTable stable){
		
		currMethod = currClass.getMethod(Helper.methodName(n));
		String m_type = Helper.getType(n.f1);
		String returnType = n.f10.accept(this, stable);
		if(m_type != null || m_type != "" || returnType != null || returnType != "" ){
			returnType = getIDType(returnType);
			if(m_type != returnType){
				anyErrors = true;
				return null;
			}
		}
		n.f8.accept(this, stable);
		return null;
	}

	public String visit(Statement n, SymbolTable stable) {
		n.f0.choice.accept(this, stable);
		return null;
	}

	public String visit(AssignmentStatement n, SymbolTable stable) {
		String id = Helper.getId(n.f0);
		String id2 = n.f2.accept(this, stable);
		if(id != null && id2 != null){
			String id_type = getIDType(id);
			String id_type2 = getIDType(id2);
			if(id_type != id_type2 || id_type == null || id_type2 == null){
				anyErrors = true;
			}
		}
		
		return null;
	}

	public String visit(PrintStatement n, SymbolTable stable) {
		String statementType = getIDType(n.f2.accept(this, stable));
		if(statementType != "int"){
			anyErrors = true;
		}
		return null;
	}

	public String visit(Expression n, SymbolTable stable){
		return n.f0.choice.accept(this, stable);
	}

	public String getIDType(String x){
		if(st.getClass(x) != null){
			return x;	
		}
		else{
			if(x == "int" || x == "boolean"){
				return x;
			}
			String name = "";
			if(currMethod.getLocalType(x) != null){
				name = currMethod.getLocalType(x);
			}
			else if(currMethod.getParamType(x) != null){
				name = currMethod.getParamType(x);
			}
			else if(currClass.getField(x) != null){
				name = currClass.getField(x);
			}
			return name;
		}
	}

	public String visit(MessageSend n, SymbolTable stable){
		
		String c_name = getIDType(n.f0.accept(this, stable));
		String m_name = Helper.getId(n.f2);
		ClassSymbol c_Class = st.getClass(c_name);
		if(c_Class != null){
				MethodSymbol c_Method = c_Class.getMethod(m_name);
			if(c_Method == null){
				anyErrors = true;
				return "";
			}
			else{
				if(n.f4.node != null){
					int callSize = Helper.getListSize(n.f4.node);
					int methodSize = c_Method.paramSize();
					if(methodSize != callSize){
						anyErrors = true;
					}
				}
				String m_type = c_Method.getMethodType();
				return m_type;
			}
		}
		else{
			anyErrors = true;
			return "";
		}
		
		
	}

	public String visit(PrimaryExpression n, SymbolTable stable){
		String c_name = n.f0.choice.accept(this, stable);
		return c_name; 
	}

	public String visit(ArrayLookup n, SymbolTable stable){
		String id1 = getIDType(n.f0.accept(this, stable));
		String id2 = getIDType(n.f2.accept(this, stable));
		if(id1 == "int []" && id2 == "int"){
			return id2;
		}
		else{
			anyErrors = true;
			return "";
		}
	}

	public String visit(AllocationExpression n, SymbolTable stable){
		String c_name = Helper.getId(n.f1);
		return c_name;
	}

	public String visit(IntegerLiteral n, SymbolTable stable){
		String c_name = "int";
		return c_name;
	}

	public String visit(FalseLiteral n, SymbolTable stable){
		String c_name = "boolean";
		return c_name;
	}

	public String visit(TrueLiteral n, SymbolTable stable){
		String c_name = "boolean";
		return c_name;
	}

	public String visit(ThisExpression n, SymbolTable stable){
		return currClass.getClassId();
	}

	public String visit(BracketExpression n, SymbolTable stable){
		return n.f1.accept(this, stable);
	}

	public String visit(PlusExpression n, SymbolTable stable){
		String first = getIDType(n.f0.accept(this, stable));
		String second = getIDType(n.f2.accept(this, stable));
		if(first == second){
			return first;
		}
		else{
			return null;
		}
	}

	public String visit(MinusExpression n, SymbolTable stable){
		String first = getIDType(n.f0.accept(this, stable));
		String second = getIDType(n.f2.accept(this, stable));
		if(first == second){
			return first;
		}
		else{
			return null;
		}
	}

	public String visit(TimesExpression n, SymbolTable stable){
		String first = getIDType(n.f0.accept(this, stable));
		String second = getIDType(n.f2.accept(this, stable));
		if(first == second){
			return first;
		}
		else{
			return null;
		} 
	}

	public String visit(Identifier n, SymbolTable stable){
		return Helper.getId(n);
	}



}