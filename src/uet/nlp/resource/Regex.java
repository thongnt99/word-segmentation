package uet.nlp.resource;

public class Regex {
	
	private String str;
	private String type;
	
	public Regex(String str,String type){
		this.str = str;
		this.type = type;
	}
	
	public String getString(){
		return this.str;
	}
	public String getType(){
		return this.type;
	}
}
