package typechecker;

import java.util.*;

public class ClassSymbol {
	private Symbol name;
	private HashMap<Symbol, String> fields;
	private HashMap<String, MethodSymbol> methods;
	private LinkedHashMap<String, Integer> vTable; 
	private Integer offset;
	private LinkedHashMap<String, Integer> record; 
	private Integer recordOffset;

	public ClassSymbol(String n) {
		name = Symbol.symbol(n);
		fields = new HashMap<Symbol, String>();
		methods = new HashMap<String, MethodSymbol>();
		//this.vTable = new ArrayList<String>();
		this.vTable = new LinkedHashMap<String,Integer>();
		this.record = new LinkedHashMap<String,Integer>();
		offset = 0; 
		recordOffset = 4;
	}

	public String getClassId() {
		return this.name.toString();
	}

	public void addMethod(String name, String type) {
		this.methods.put(name, new MethodSymbol(name, type));
		//vTable.add(name);
		vTable.put(name,offset);
		offset = offset + 4; 
	}
	
	public Integer getOffset(String name){
		Integer val = 0; 
		if(vTable.containsKey(name)){
			val = vTable.get(name);
		}
		return val;
	}

	public HashMap<Symbol, String> getFields(){
		return this.fields;
	}

	public void setFields(HashMap<Symbol, String> s){
		this.fields = s;
	}

	public LinkedHashMap<String, Integer> getRecord(){
		return this.record;
	}
	
	public int recordSize(){
		return record.size();
	}
	public void SetRecord(LinkedHashMap<String, Integer> r){
		this.record = r;
	}

	public LinkedHashMap<String, Integer> getvTable(){
		return vTable;
	}

	public void addFields(String name, String type) {
		this.fields.put(Symbol.symbol(name), type);
		record.put(name, recordOffset);
		recordOffset = recordOffset + 4;
	}

	public boolean checkRecord(String name){
		if(record.containsKey(name)){
			return true;
		}
		return false;
	}

	public Integer getRecordOffset(String name){
		return record.get(name);
	}

	public MethodSymbol getMethod(String name) {
		return this.methods.get(name);
	}

	public String getField(String name) {
		return this.fields.get(Symbol.symbol(name));
	}

	public int methodSize() {
		return methods.size();
	}

	public int fieldSize(){
		return fields.size();
	}

	public Set<String> getMethodNames() {
		return vTable.keySet();
	}

}