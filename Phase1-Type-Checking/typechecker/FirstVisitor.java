package typechecker;

import syntaxtree.*;
import visitor.*;

public class FirstVisitor extends DepthFirstVisitor {
  public void visit(Goal n) {

    // check if all classes have different name
    // if(all clases different named){
    NodeList classNames = new NodeList(n.f0); // starts with the main class in it
    if (n.f1.size() > 0) {
      for (int i = 0; i < n.f1.size(); i++) {
        classNames.addNode(((TypeDeclaration) n.f1.elementAt(i)).f0.choice);
      }
    }

    if (Helper.classDistinct(classNames)) {
      n.f0.accept(this);
      n.f1.accept(this);
    } else {
      // PROBLEM
    }
  }

  public void visit(MainClass n) {
    n.f14.accept(this);
  }

  public void visit(TypeDeclaration n) {
    // if all id are different
    // all methood names are different
    // if()
    n.f0.accept(this);
  }

  public void visit(ClassDeclaration n) {
    System.out.println(n);
    n.f3.accept(this);
    n.f4.accept(this);
  }

  public void visit(MethodDeclaration n) {
    // check if all parameters are different named
    // check if all variables inside are different named
    n.f7.accept(this);
  }

  public void visit(VarDeclaration n) {

  }
}
