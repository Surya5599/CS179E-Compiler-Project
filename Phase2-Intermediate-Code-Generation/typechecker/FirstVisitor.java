package typechecker;

import java.util.HashSet;
import java.util.Set;

import syntaxtree.*;
import visitor.*;

class ErrorMsg {
  boolean anyErrors;
  void complain(String msg) {
    anyErrors = true;
    //System.out.println(msg);
  }
}

public class FirstVisitor extends DepthFirstVisitor {
  ErrorMsg error = new ErrorMsg();
  private SymbolTable st = new SymbolTable();
  private ClassSymbol currClass;
  private MethodSymbol currMethod;

  public boolean getErrors() {
    return error.anyErrors;
  }

  public SymbolTable getSymbolTable() {
    return this.st;
  }

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
    // CHECK IF ALL ID ARE DIFFERENT need a function that has a NodeList of
    // Identifier
    // and check if they all different /there is already a identifier name there
    String cname = Helper.className(n);
    st.addClass(cname);
    currClass = st.getClass(cname);
    currClass.addMethod("main", "void");
    currMethod = currClass.getMethod("main");
    n.f14.accept(this);
  }

  public void visit(ClassDeclaration n) {
    // check all ID are different Use same function n.f3
    // check if all methods are different n.f4
    String cname = Helper.className(n);
    st.addClass(cname);
    currClass = st.getClass(cname);
    currMethod = null;
    if (Helper.idDistinct(n.f3)) {
      n.f3.accept(this);
    } else {
      error.complain("ID names are same");
    }
    if (Helper.MethodDistinct(n.f4)) {
      n.f4.accept(this);
    } else {
      error.complain("Method names are same");
    }
    
  }

  public void visit(ClassExtendsDeclaration n) {
    // check all ID are different use same function n.f5
    // check if all methods are different //Method Declaration distinct n.f6
    String cname = Helper.className(n);
    st.addClass(cname);
    currClass = st.getClass(cname);
    currMethod = null;
    String superClass = Helper.getId(n.f3);
    ClassSymbol super_class = st.getClass(superClass);
    currClass.SetRecord(super_class.getRecord());
    currClass.setFields(super_class.getFields());
    Set<String> super_methods = super_class.getMethodNames();
    if(Helper.noOverloading(super_methods, n.f6)){
      if (Helper.idDistinct(n.f5)) {
        n.f5.accept(this);
      } else {
        error.complain("ID names are same");
      }
      if (Helper.MethodDistinct(n.f6)) {
        n.f6.accept(this);
      } else {
        error.complain("Method names are same");
      }
    } else {
      error.complain("Overloaded method");
    }
    
    

  }

  public void visit(MethodDeclaration n) {
    // check if all parameters are different named n.f4
    // check if all variables inside are different named n.f6
    currClass.addMethod(Helper.methodName(n), Helper.methodType(n));
    currMethod = currClass.getMethod(Helper.methodName(n));
    if (n.f4.node != null) {
      if (Helper.parameterDistinct((FormalParameterList) n.f4.node)) {
        n.f4.accept(this);
      } else {
        error.complain("{Params} names are same");
      }
    }
    if (Helper.idDistinct(n.f7)) {
      n.f7.accept(this);
    } else {
      error.complain("ID names are same");
    }
  }

  public void visit(VarDeclaration n) {
    if(currMethod == null && currClass != null){
      currClass.addFields(Helper.getId(n.f1), Helper.getType(n.f0));
    }
    else if (currMethod != null) {
      currMethod.addLocal(Helper.getId(n.f1), Helper.getType(n.f0));
    }
  }

  public void visit(FormalParameter n){
    if(currMethod != null && currClass != null){
      currMethod.addParam(Helper.getId(n.f1), Helper.getType(n.f0));
    }
  }
}
