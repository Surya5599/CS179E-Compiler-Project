package typechecker;

import java.util.*;

import syntaxtree.*;
import visitor.*;

public class Translation extends GJNoArguDepthFirst<String>{
	
	private SymbolTable stable;
	private Printer printer;
	private ClassSymbol currClass;
	private MethodSymbol currMethod;
	private boolean allocationFunction;
	

	public Translation(SymbolTable st){
		this.stable = st;
		this.allocationFunction = false;
		this.printer = new Printer();
		List<String> classList = st.getClassList();
		for(int i = 1; i < classList.size(); i++){
			printer.classes(classList.get(i), st.getClass(classList.get(i)).getMethodNames());
		}
		printer.addEnter();
	}

	public String visit(Goal n){
		n.f0.accept(this);
		n.f1.accept(this);
		if(allocationFunction == true){
			printer.newArray();
		}
		return null;
	}

	public String visit(MainClass n){
		printer.main();
		n.f14.accept(this);
		n.f15.accept(this);
		printer.ret("");
		printer.addEnter();
		return null;
	}

	public String visit(ClassDeclaration n){
		currClass = stable.getClass(n.f1.accept(this));
		n.f3.accept(this);
		n.f4.accept(this);
		return null;
	}

	public String visit(ClassExtendsDeclaration n){
		currClass = stable.getClass(n.f1.accept(this));
		n.f5.accept(this);
		n.f6.accept(this);
		return null;
	}

	public String visit(MethodDeclaration n){
		String className = currClass.getClassId();
		String mName = n.f2.accept(this);
		String params = n.f4.accept(this);
		currMethod = currClass.getMethod(mName);
		printer.methodDeclare(className, mName, params);
		n.f8.accept(this);
		String ret = n.f10.accept(this);
		if(currClass.checkRecord(ret) == true){
			ret = printer.getRecord(currClass.getRecordOffset(ret));
		}
		printer.ret(ret);
		printer.addEnter();
		//printer.function();
		return null;
	}


	public String visit(Identifier n){
		return n.f0.toString();
	}

	public String visit(NotExpression n){
		return printer.subtract("1", n.f1.accept(this));
	}



	public String visit(PrintStatement n) {
		printer.output((n.f2.accept(this)));
		return null;
	}

	public String visit(Expression n){
		return n.f0.choice.accept(this); 
	}

	public String visit(PrimaryExpression n){
		return n.f0.choice.accept(this); 
	}

	public String visit(IntegerLiteral n){
		String info = n.f0.toString();
		return info;
	}

	public String visit(PlusExpression n){
		String first = n.f0.accept(this);
		String second = n.f2.accept(this);
		first = recordVariableCheck(first);
		second = recordVariableCheck(second);
		return printer.add(first, second);
	}

	public String visit(MinusExpression n){
		String first = n.f0.accept(this);
		String second = n.f2.accept(this);
		first = recordVariableCheck(first);
		second = recordVariableCheck(second);
		return printer.subtract(first, second);
	}

	public String visit(TimesExpression n){
		String first = n.f0.accept(this);
		String second = n.f2.accept(this);
		first = recordVariableCheck(first);
		second = recordVariableCheck(second);
		return printer.mult(first, second);
	}

	public String visit(MessageSend n){
		String cVar = n.f0.accept(this);
		String cName = printer.getClassName(cVar);
		if(cName == null){
			cName = getIDType(cVar);
		}
		ClassSymbol cs = stable.getClass(cName);
		if(cs == null){
			cs = currClass;
		}
		String methodName = n.f2.accept(this);
		String param = n.f4.accept(this);
		cVar = recordCheck(cVar);
		String finalVar = printer.functionCall(cVar, cs.getOffset(methodName), param);
		return finalVar;
	}

	public String getIDType(String x){
		if(stable.getClass(x) != null){
			return x;	
		}
		else if(x == "this"){
			return currClass.getClassId();
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

	public String visit(AllocationExpression n){
		return printer.allocation(n.f1.accept(this), stable.getClass(n.f1.accept(this)).recordSize());
	}

	public String visit(FormalParameterList n){
		String params = n.f0.accept(this);
		params = params + n.f1.accept(this);
		return params;
	}

	public String visit(ExpressionList n){
		String params = n.f0.accept(this);
		params = params + n.f1.accept(this);
		return params;
	}

	public String visit(ExpressionRest n){
		String s = n.f1.accept(this);
		s = recordVariableCheck(s);
		return s;
	}

	public String visit(FormalParameter n){
		String s = n.f1.accept(this);
		s = recordVariableCheck(s);
		return s;
	}

	public String visit(FormalParameterRest n){
		String s = n.f1.accept(this);
		s = recordVariableCheck(s);
		return s;
	}

	public String visit(NodeListOptional n){
		String _ret = "";
		for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
			_ret = _ret + " " + e.nextElement().accept(this);
		}
		return _ret;
	}

	public String visit(Statement n){
		n.f0.accept(this);
		return null;
	}

	public String visit(AssignmentStatement n){
		String first = n.f0.accept(this);
		String second = n.f2.accept(this);
		if(second.contains("call :")){
			second = printer.callFunc(second);
			first = recordCheck(first);
		}
		else{
			first = recordCheck(first);
		} 
		second = recordCheck(second);
		printer.assign(first, second);
		return null ;
	}

	public String visit(ThisExpression n){
		return n.f0.toString();
	}

	public String visit(ArrayAllocationExpression n){
		this.allocationFunction = true;
		String s = "call :AllocArray(" + n.f3.accept(this) + ")";
		return s;
	}

	public String visit(ArrayLookup n){
		String name = n.f0.accept(this);
		String loc = n.f2.accept(this);
		name = recordVariableCheck(name);
		return printer.lookup(name, loc);
	}

	public String visit(BracketExpression n){
		String s = n.f1.accept(this);
		//s = recordCheck(s);
		return s;
	}

	public String visit(IfStatement n){
		String s = n.f2.accept(this);
		s = recordVariableCheck(s);
		String if1 = s;
		String label = printer.beginIf(if1);
		n.f4.accept(this);
		printer.endIf(label);
		printer.beginIfElse(label);
		n.f6.accept(this);
		printer.endIfElse(label);
		return null;
	}

	public String visit(AndExpression n){
		String s = n.f0.accept(this);
		String l = printer.beginSS(s);
		String var = n.f2.accept(this);
		printer.continueSS(l);
		printer.noWork(var);
		printer.endSS(l);
		return var;
	}

	public String visit(CompareExpression n){
		String first = n.f0.accept(this);
		String second = n.f2.accept(this);
		first = recordVariableCheck(first);
		second = recordVariableCheck(second);
		return printer.lts(first, second);
	}

	public String recordCheck(String s){
		if(currClass != null){
			if(currClass.checkRecord(s) == true){
				s = "[this+" + currClass.getRecordOffset(s) + "]";
			}
		}
		return s;
	}

	public String recordVariableCheck(String s){
		if(currClass != null){
			if(currClass.checkRecord(s) == true){
				s = printer.getRecord(currClass.getRecordOffset(s));
			}
		}
		
		return s;
	}

	public String visit(ArrayAssignmentStatement n){
		String first = recordCheck(n.f0.accept(this));
		String var = printer.arrayPrint(first, n.f2.accept(this));
		var = "[" + var + "+4]";
		String equals = n.f5.accept(this);
		printer.assign(var, equals);
		return null;
	}

	public String visit(FalseLiteral n){
		return "0";
	}

	public String visit(TrueLiteral n){
		return "1";
	}

	public String visit(WhileStatement n){
		String currLabel = printer.beginWhile();
		String varlabel = n.f2.accept(this);
		printer.continueWhile(currLabel, varlabel);
		n.f4.accept(this);
		printer.endWhile(currLabel);
		return null;
	}

}
