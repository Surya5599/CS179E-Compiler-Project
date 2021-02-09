# CS179E-Compiler-Project
Surya Singh
ssing
862033627
Neha Gupta
ngupt
862049507

# Phase 1 Report

## Requirements:

Phase 1 is a typechecker for miniJava. When running a file the typechecker verifies at compile
time that discrepancies do not occur during run-time. It will result in a pass or fail after
reviewing all possible scenarios. The typechecker follows a typesystem. In this we extend two
visitors in order to type check the java rules. If the types are implemented correctly in the java
file, we print “​Program type checked successfully”​ or if it doesn’t we have a “​Type error”​.

## Design

## Type checking

Our typechecker has two parts. First part uses the DepthFirstVisitor, in order to check a few rules
of method declaration and id declaration along with building a symbol tree. Second Part extends
the GJDepthFirst visitor in order to type check statements and expressions.

## Project Overview and File Structure

● **Typecheck.java** ​- Main file which calls two of the parts in order to typecheck a java file.
Created a package typechecker which holds inside:
● **FirstVisitor.java** ​- This is part 1 which extends the DepthFirstVisitor to create Symbol
Table
● **SecondVisitor.java** ​- This is the second part which extends GJDepthFirstVisitor to type
check statements.
● **Helper.java** ​ - This file contains the helper functions such as distinct or getID
● **SymbolTable.java** ​- This is the symbol table implementation described later.
● **ClassSymbol.java** ​- This holds the implementation of a class symbol.
● **MethodSymbol.java** ​ - This holds the implementation of a method symbol.
● **Symbol.java** ​- This file is used to create symbols.


## Main File

In our typecheck file, we first get the root node(goal) of the file passed in, and then create a
FirstVisitor(ft) object. Then we call the accept function on the root, passing in the Firstvisitor
object. Which begins our part 1 of building the symbol table. Once building the symbol table is
completed, we check if there were any errors, and if there were, then we have a type error. If
there are no errors, then we move on to the next part which is type checking the statements and
expressions. We create a SecondVisitor object, and this time when we call accept on the root, we
pass in the second visitor object, along with the symboltable, that we fetch from the FirstVisitor
object. Once the type checking completes, we check if there are any errors, if so then we have a
type error. If there are no errors, that completes the type checking.

## How does our SymbolTable work?

In order to build the SymbolTable, we used this implementation that's described in the book[1].
A class has fields and methods, and the methods have their own params and locals. So we
decided to create objects for each of these. In a SymbolTable, it has a HashMap that maps a
String to a ClassSymbol Object. Which allows us to map a class name to a ClassSymbol object.
Our ClassSymbol object contains the name of the class, a hashmap for fields which is the
variable declaration inside of classes, and a hashmap for methods, which maps a name of the
method to its method symbol. Our MethodSymbol object contains the name of the method along
with its type, a hashmap for the local variables and hashmap for parameter variables. Each of
these hashmaps hash a Symbol(name) to a String(type). Each of these classes have their
respective setter and getter functions which allow us to later traverse through the symbolTable.

## First Part - FirstVisitor extends DepthFirstVisitor

In part 1 of the type checking phase we extended the DepthFirstVisitor class and implemented
some of the functions that look at type rules:- Goal, MainClass, ClassDeclaration,
ClassExtendsDeclaration, MethodDeclaration, VariableDeclaration, and more. We created visit


methods of each of the types that needed to be processed and contained information for the
symbol table. This class has a private SymbolTable, which it builds as the visitor extends deeper
into the program. Using the accept(this) method we were able to extend specific methods and
classes and look for symbols within them. This class uses the Helper function, in order to process
a Node before it is implemented inside the symbol table. First we check for things such as if the
classnames are all unique and then if all methods are unique and look for any errors within each
implementation. After everything is correct with the names, we process the current thing to be
implemented inside the symbol table. We also created two private variables, currClass and
currMethod, in order to keep track of which class or which method is currently being processed,
so that when we build the symbol table we know, which class to add the fields and methods and
which method to add the params and locals.
Example of a function in FirstVisitor.java
This is one example of how the functions in first visitor work, this function looks at the
ClassDeclaration node type and before processing the methods and variables inside, we first
check if all id are distinct which we call using Helper.idDistinct and pass in, and same thing for
Methods we call Helper.methodDistinct and pass in the MethodDeclaration NodeList.

## Second Part- SecondVisitor extends GJDepthVistor

In part 1 of the type checking phase we extended the DepthFirstVisitor class and implemented
some of the functions that expand statements and check the type rules using the symbol table.
This file also has a private currClass and currMethod, these will be used to keep track of which


class the statement is in or the class and the method the statement is in. All of the visit functions
in this file are a String type, this is done because later on we can return the type of an object
using the string. For example, if there is an assignment statement, x = 1, we can visit the
Identifier x = 1, which can get the Id of x using the symbol table and then return “int” for x and
same thing goes for the 1, which is IntegerLiteral, and return int for that and then assignment will
check if these two are correct. If at any point we don't find something in the symbol table or if a
type doesn’t match the rule, we have a boolean that is set to true, which we check later in the
typecheck.java.
Example for a visit function within SecondVisitor.java
This is an example of the visit functions for the PlusExpression, where we look at the left side
and the right side and call this getIDType, function which looks at the string passed in, and looks
it up in the symbol table and if it exists it return the type of the object that exists in the current
class or current method.

## Helper Functions

Get name Functions: ​The get name functions takes a type method, class, and returns a string.
GetIntegerType, getID, getMethodName.
ClassName: ​The class name helper function checks first for the type of class declaration whether
it is a main class, class, or class extends. Then once the type is determined the function returns
the name.
Distinct: ​This reads in a list of class names and we use a hashSet to keep track of duplicate
names. If any duplicate name occurs the function returns false and returns an error.
For the method, class, and id distinct we call the respective helper functions which return the
name. Then check if the name already exists in the Set.
For parameter distinct we have a formal parameter and a nodeList optional types. The formal
parameter refers to one parameter and nodeList optional is the possible different parameters in
the List.


NoOverloading:​ This function checks the extended class and if there are no overloaded
functions. It takes a string set parameter which is the method name of the super class and it has a
NodeList which has the methods from the current class.

## Testing and Verification

We used the given test files to check our program, and oftentimes we also expanded on them in
order to test specific functions as we created them.

## References

1. https://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.461.6592&rep=rep1&type=pdf

# Report Phase 2

## Requirements and Specifications
 In this phase we want to explore the lower levels of the compiler creation for MiniJava. We translate the MiniJava language to Vapor language which is an assembly like language but without the limitation of finite registers. In this phase we translate a .java file to a compilable .vapor file.
Though vapor has multiple built in functions, for this phase we only use these specific functions:
● Basic Arithmetic: ​ Add, Sub, MulS
● Comparison: ​ Eq, Lt, LtS
● Displaying Output: ​ PrintIntS
● Memory Allocation: ​ HeapAllocZ
● Error: ​ Error

## Design

### Vapor Translation
To translate the java file to a vapor file, we extended another visitor GJNoArguDepthFirst Visitor, in order to translate the classes, methods, variables to vapor language and also used the String return type in order to build the file recursively. In this part we added onto the previous phase which type checks the java program. Once the type checking is done, it is translated into a vapor file using the visitor.

### Project Overview and File Structure

This part has the same file structure that was in phase 1, which can be found here:
https://github.com/Surya5599/CS179E-Compiler-Project/blob/main/Phase1-Type-Checking/Pha
se1_report.pdf
Along with the phase 1 files we added 3 additional files:


● J2V.java ​ - Changed the typecheck.java file to J2V file and after the two type checking visitors are called instead of saying the checking was successful, we called the translation visitor and passed in the symbol table to print.
 ● Translation.java ​ - We extend the ​G​JNoArguDepthFirst ​in order to translate to vapor file
● Printer.java ​ - This is used as a helper class in order to print to the vapor file.

### Translation

Once the translation visitor is called, the first thing it does it initializes, the private SymbolTable of the visitor. Then it calls the vtable, in order to define the data segments (const class definition with its methods) for each class except the main class. Once the data segments are created, we use the visit functions in order to process the java file and convert methods and statements into vapor language. This class is used to extract the information about a statement, and finally uses the printer class in order to convert the information into the vapor translation. Once we reach a Statement, we determine which type of statement it is and extract the needed information from it. For example, if its an assignment statement, we extract the variable from the left side, then the variable from the right side. Once that is extracted, we check if the variable is part of the class record if it is, we convert it to be used as a memory address by getting the offset. Once we have both of the variables, we pass this information to the printer class by calling the corresponding print function, which then creates variables for the needed parameters in the vapor language and prints the vapor language into the file. 
This is the sample for the AssignmentStatement, which shows the implementation described above.


### Record and v-Table Implementation

To create the record table and the vtable, we added more functions and objects to the ClassSymbol class. We decided to build the vTable and the Record Table during the type checking process because we want to keep the order for when each method is called. 

To build the v-table the data structure used is a linked hashmap. A linked hash-map is a hashmap and linked list to create a map. This structure will keep track of insertion order and was necessary to keep track of the variables and the corresponding offset. As the addMethod function is called it adds the name to the v-table along with the offset. With a global variable offset set to 0 after every added name it increments by 4 for the next method. There is also a getOffset() method which returns the corresponding offset. It checks if the LinkedHashmap contains the name and gets the value and returns it. 

To build the record table the structure is similar to the v-table. It also uses a linked hash-map to keep track of the fields and its corresponding offset. We created a getRecord which returns the table. There is an addFields table which puts the names into the record table with the offset, similar to addMethod in v-table. CheckRecord also verifies whether the name exists in the table and returns the corresponding offset. 


### Printer Class

We created a printer class which is used to print to the vapor file, and contains the specification of what the vapor file looks like. We defined multiple different functions for specific for example, functions for if statements, while loops, method calls, object creation, operations and etc. This class also has multiple counters for different labels. We created a variable counter, label counter, null label counter, ifelse counter, and a while counter. This is just a simple integer counter which is used in the creation of any of the labels in order to not reuse labels throughout the program. There is also a depth counter, which is used to print the additional spaces when we are within a method call or if statements or while statements. All of the functions take in some String parameters based on which kind of function it is. Some of the functions have a return type of String, because when we do some operations, we want to be able to return the variable the answer is stored in. For example when we do Add(5 4), we set it equal to t.0 = Add(5 4), and return that t.0 so it can be used later for example to print or to  use it for other operations. Here is the example of add function.

Here is the example of array lookup:
Similar to the function above, we created multiple functions that print the vapor language.

## Testing and Verification

For testing and verification purposes, we used the given test cases, which are shown as passing below, along with our own P.java file, which we adjusted to test specific functionality as we created them.
