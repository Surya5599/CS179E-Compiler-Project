help:
	@echo "  V2VM:	Generate the parser."
	@echo "  typecheck:	Generate the Typecheck file and run it."
	@echo "  clean:	Remove auto-generated files."


V2VM: V2VM.java 
		javac -cp "./:./Support/vapor-parser.jar" V2VM.java
		java -cp "./:./Support/vapor-parser.jar" V2VM < P.vapor > P.vaporm

clean:
			rm -rf *.class
			rm -rf *.tgz
			rm -rf RegisterAllocation/*.class
			rm -rf Phase3Tests/Output


test: V2VM.java 
	mkdir hw3
	cp V2VM.java hw3/
	cp -R RegisterAllocation/ hw3/RegisterAllocation
	tar zcf hw3.tgz hw3/
	rm -rf hw3
	chmod +x Phase3Tests/run
	Phase3Tests/./run Phase3Tests/SelfTestCases hw3.tgz