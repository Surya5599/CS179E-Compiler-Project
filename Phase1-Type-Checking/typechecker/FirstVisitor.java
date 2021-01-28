package typechecker;

import syntaxtree.*;
import visitor.*;

class ErrorMsg {
  boolean anyErrors;

  void complain(String msg) {
    anyErrors = true;
    System.out.println(msg);
  }
}

public class FirstVisitor extends DepthFirstVisitor {
  ErrorMsg error;
  SymbolTable st = new SymbolTable();
  ClassSymbol currClass;
  MethodSymbol currMethod;

  public void visit(Goal n) {

    NodeList classNames = new NodeList(n.f0);
    if (n.f1.size() > 0) {
      for (int i = 0; i < n.f1.size(); i++) {
        classNames.addNode(((TypeDeclaration) n.f1.elementAt(i)).f0.choice);
      }
    }

    if (Helper.classDistinct(classNames)) {
      n.f0.accept(this);
      n.f1.accept(this);
    } else {
      error.complain("Class names are same");
    }
  }

  public void visit(MainClass n) {

    //CHECK IF ALL ID ARE DIFFERENT need a function that has a NodeList of Identifier and check if they all different /there is already a identifier name there 
    String cname = Helper.className(n);
    st.addClass(cname);
    currClass = st.getClass(cname);
    currClass.addMethod("main", "void");
    currMethod = currClass.getMethod("main");
    n.f14.accept(this);
  }

  public void visit(ClassDeclaration n) {
    //check all ID are different Use same function 
    //check if all methods are different
    String cname = Helper.className(n);
    st.addClass(cname);
    currClass = st.getClass(cname);
    n.f3.accept(this);
    n.f4.accept(this);
  }

  public void visit(ClassExtendsDeclaration n) {
    //check all ID are different use same function 
    //check if all methods are different  //Method Declaration distinct
    String cname = Helper.className(n);
    st.addClass(cname);
    currClass = st.getClass(cname);
  }

  public void visit(MethodDeclaration n) {
    // check if all parameters are different named
    // check if all variables inside are different named
    currClass.addMethod(Helper.methodName(n), Helper.methodType(n));
    currMethod = currClass.getMethod(Helper.methodName(n));
    n.f7.accept(this);
  }

  public void visit(VarDeclaration n) {
    if (currMethod != null) {
      currMethod.addLocal(Helper.getId(n.f1), Helper.getType(n.f0));
    }
  }
}
