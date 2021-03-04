help:
	@echo "  VM2M:	Generate the parser."
	@echo "  typecheck:	Generate the Typecheck file and run it."
	@echo "  clean:	Remove auto-generated files."


VM2M: VM2M.java 
		javac -cp "./:./Support/vapor-parser.jar" VM2M.java
		java -cp "./:./Support/vapor-parser.jar" VM2M < P.vaporm > P.s

clean:
			rm -rf *.class
			rm -rf *.tgz
			rm -rf RegisterAllocation/*.class
			rm -rf Phase3Tests/Output


test: VM2M.java 
	mkdir hw4
	cp VM2M.java hw4/
	cp -R RegisterAllocation/ hw4/RegisterAllocation
	tar zcf hw4.tgz hw4/
	rm -rf hw4
	chmod +x Phase4Tests/run
	Phase3Tests/./run Phase3Tests/SelfTestCases hw4.tgz