package typechecker;
import java.io.*;
import java.util.*;
//package Symbol;


public class Symbol {
   private String name;
   private Symbol(String n) {name=n; }
   private static java.util.Dictionary dict = new java.util.Hashtable();
   public String toString() {return name;}
   public static Symbol symbol(String n) {
       String u = n.intern();
       Symbol s = (Symbol)dict.get(u);
       if (s==null) {s = new Symbol(u); dict.put(u,s); }
       return s;
   }
}
/*public class Table {
 public Table(){
   Hashtable<Symbol, Object> symbolTable = new Hashtable<>();
 }
 public Table put(Symbol key, Object value){
   return symbolTable.put(key, value);
 }
 public Object get(Symbol key){
   return symbolTable.get(key);
 }
 /*public java.util.Enumeration keys(){

 }
}*/
