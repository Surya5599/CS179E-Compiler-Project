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
