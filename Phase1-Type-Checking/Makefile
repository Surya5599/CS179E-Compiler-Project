help:
	@echo "  build:	Generate the parser."
	@echo "  typecheck:	Generate the Typecheck file and run it."
	@echo "  clean:	Remove auto-generated files."
	@echo "  test:	Test code with phase1 tester."

build: minijava.jj jtb.jar
		java -jar jtb.jar minijava.jj
		javacc jtb.out.jj

typecheck: Typecheck.java
		javac Typecheck.java
		java Typecheck < P.java

test: Typecheck.java
	mkdir hw1
	cp Typecheck.java hw1/
	cp -R typechecker/ hw1/typechecker
	tar zcf hw1.tgz hw1/
	rm -rf hw1
	chmod +x Phase1Tester/run
	Phase1Tester/run Phase1Tester/SelfTestCases hw1.tgz

clean:
			rm -rf *.class
			rm -rf *.tgz
			rm -rf Phase1Tester/Output
			rm -rf typechecker/*.class
