help:
	@echo "  build:	Generate the parser."
	@echo "  typecheck:	Generate the Typecheck file and run it."
	@echo "  clean:	Remove auto-generated files."
	@echo "  test:	Test code with phase1 tester."

build: minijava.jj jtb.jar
		java -jar jtb.jar minijava.jj
		javacc jtb.out.jj

J2V: J2V.java
		javac J2V.java
		java J2V < P.java > P.vapor

test: J2V.java
	mkdir hw2
	cp J2V.java hw2/
	cp -R typechecker/ hw2/typechecker
	tar zcf hw2.tgz hw2/
	rm -rf hw2
	chmod +x Phase2Tester/run
	Phase2Tester/./run Phase2Tester/SelfTestCases hw2.tgz

vapor: vapor.jar p.vapor
		java -jar vapor.jar run p.vapor

clean:
			rm -rf *.class
			rm -rf *.tgz
			rm -rf Phase2Tester/Output
			rm -rf typechecker/*.class
