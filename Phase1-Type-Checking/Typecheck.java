import syntaxtree.*;
import visitor.*;
import typechecker.*;

public class Typecheck {
   public static void main(String[] args) {
      try {
         new MiniJavaParser(System.in);
         Node root = MiniJavaParser.Goal();
         root.accept(new FirstVisitor());
         System.out.println("Program type checked successfully");
      } catch (ParseException e) {
         System.out.println("Type error");
      }
   }
}
