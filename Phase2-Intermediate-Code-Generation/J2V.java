import syntaxtree.*;
import typechecker.*;

public class J2V {
   public static void main(String[] args) {
      try {
         new MiniJavaParser(System.in);
         Node root = MiniJavaParser.Goal();
         FirstVisitor ft = new FirstVisitor();
         root.accept(ft);
         if (ft.getErrors() == true) {
            System.exit(1);
         } else {
            SecondVisitor sv = new SecondVisitor(ft.getSymbolTable());
            root.accept(sv, ft.getSymbolTable());
            if(sv.getErrors() == true){
               System.exit(1);
            }
            else{
               Translation tv = new Translation(ft.getSymbolTable());
               root.accept(tv);
            }
            
         }
      } catch (ParseException e) {
         System.exit(1);
      }
   }
}
