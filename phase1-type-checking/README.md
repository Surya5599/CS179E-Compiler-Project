#Phase 1: Type-checking#

##Introduction##
MiniJava is a subset of java that includes the bare minimum of Java: integers, integer arrays, classes, subclasses, and printing integers to standard out. It does not permit any float types, strings, overloading and overriding methods, or any interfaces. It has a few other restrictions, but those are minor. Thus, the MiniJava statement System.out.println(...); can only print integers. The MiniJava expression e.length only applies to expressions of type int[].

A sample MiniJava program is Factorial.java. Download

```class Factorial{
   public static void main(String[] a){
      System.out.println(new Fac().ComputeFac(10));
   }
}

class Fac {
   public int ComputeFac(int num){
      int num_aux;
      if (num < 1)
         num_aux = 1;
      else
         num_aux = num * (this.ComputeFac(num-1));
      return num_aux;
   }
}```

Dependencies
We need to have the following:
- JavaCC (Java Compiler Compiler) parser generator. Install javacc:
   $ sudo apt-get install javacc
  Given a context free grammer, JavaCC generates a parser.

- JTB (Java Tree Builder) jar file jtb.jar: Download
  JTB is a syntax tree builder to be used with JavaCC.  Given a JavaCC grammar file, it automatically generates syntax tree and visitor classes and an annotated JavaCC grammer to build syntax trees.

- MiniJava Grammer for JavaCC minijava.jj: Download
  The context free grammer of MiniJava that is input to JTB and JavaCC.
  Here is a more readable representation of the MiniJava grammar.

- Test cases Phase1Tests.tar.gz: Download
  The test cases that we test our type checker with.

Generating AST
We must first create a parser for the MiniJava language and generate a set of syntax tree classes and visitor classes. For an overview of JTB, JavaCC and the Visitor pattern, study the following slides. Download
Complete the following steps.

   $ java -jar /path/to/jtb.jar /path/to/minijava.jj

   $ javacc jtb.out.jj

Once this is done, you will have a complete parser for MiniJava and a set of classes used for traversing the syntax tree. You will also have two different default visitors: DepthFirstVisitor and GJDepthFirst. You should extend these two visitors to type check a MiniJava program.

Type-checking
The goal of phase 1 is to write a type checker for MiniJava. Given a program, the type checker checks at compile time that type mismatch does not happen at run time. It either approves or rejects the program. The set of rules that the type checker checks are represented as a type system. You can consult the book chapter 5 on semantic analysis and the following MiniJava type system. Download

Submission
Your main file should be called Typecheck.java, and if P.java contains a program to be type checked, then:

java Typecheck < P.java

should print either Program type checked successfully or Type error.

Untar the following tarball and read the instructions in the ReadMe.txt file.
Your submission must work with the testing script when run on the department servers.  Make sure you test your final tarball before submitting.

Phase1Tester.tar.gz Download
