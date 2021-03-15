help:
	@echo "  VM2M:	Generate the parser."
	@echo "  typecheck:	Generate the Typecheck file and run it."
	@echo "  clean:	Remove auto-generated files."


VM2M: VM2M.java 
		javac -cp "./:./Support/vapor-parser.jar" VM2M.java
		java -cp "./:./Support/vapor-parser.jar" VM2M < P.vaporm > P.s

check: VM2M.java Phase5Tests/ Phase4Tests/
		javac -cp "./:./Support/vapor-parser.jar" VM2M.java
		cp Phase4Tests/$(foo).vaporm P.vaporm
		java -cp "./:./Support/vapor-parser.jar" VM2M < Phase4Tests/$(foo).vaporm > P.s
		code --diff P.s Phase5Tests/$(foo).s

clean:
		rm -rf *.class
		rm -rf *.tgz
		rm -rf RegisterAllocation/*.class
		rm -rf Phase3Tests/Output
		rm -rf hw4/


test: VM2M.java 
	mkdir hw4
	cp VM2M.java Printer.java TranslateVisitor.java hw4/
	tar zcf hw4.tgz hw4/
	rm -rf hw4
	chmod +x Phase4Tester/run
	Phase4Tester/./run Phase4Tester/SelfTestCases hw4.tgz