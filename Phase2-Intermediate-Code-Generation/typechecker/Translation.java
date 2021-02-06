package typechecker;

import java.util.*;

import syntaxtree.*;
import visitor.*;

public class Translation extends GJNoArguDepthFirst<String>{
	
	private SymbolTable stable;
	private Printer printer;
	private ClassSymbol currClass;
	private MethodSymbol currMethod;
	

	public Translation(SymbolTable st){
		this.stable = st;
		this.printer = new Printer();
		List<String> classList = st.getClassList();
		for(int i = 1; i < classList.size(); i++){
			printer.classes(classList.get(i), st.getClass(classList.get(i)).getvTable());
		}
		printer.addEnter();
	}

	public String visit(MainClass n){
		printer.main();
		n.f14.accept(this);
		n.f15.accept(this);
		printer.ret();
		printer.addEnter();
		return null;
	}

	public String visit(ClassDeclaration n){
		currClass = stable.getClass(n.f1.accept(this));
		n.f3.accept(this);
		n.f4.accept(this);
		return null;
	}

	public String visit(MethodDeclaration n){
		System.out.print(n);
		//printer.function();
		return null;
	}

	public String visit(Identifier n){
		return n.f0.toString();
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
		return printer.add(first, second);
	}

	public String visit(MinusExpression n){
		String first = n.f0.accept(this);
		String second = n.f2.accept(this);
		return printer.subtract(first, second);
	}

	public String visit(TimesExpression n){
		String first = n.f0.accept(this);
		String second = n.f2.accept(this);
		return printer.mult(first, second);
	}



}
