import syntaxtree.*;
import typechecker.*;

public class Typecheck {
   public static void main(String[] args) {
      try {
         new MiniJavaParser(System.in);
         Node root = MiniJavaParser.Goal();
         FirstVisitor ft = new FirstVisitor();
         root.accept(ft);
         if (ft.getErrors() == true) {
            System.out.println("Type error");
            System.exit(1);
         } else {
            SecondVisitor sv = new SecondVisitor(ft.getSymbolTable());
            root.accept(sv, ft.getSymbolTable());
            if(sv.getErrors() == true){
               System.out.println("Type error");
               System.exit(1);
            }
            else{
               System.out.println("Program type checked successfully");
            }
            
         }
      } catch (ParseException e) {
         System.out.println("Type error");
         System.exit(1);
      }
   }
}
