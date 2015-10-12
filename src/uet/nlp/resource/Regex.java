package uet.nlp.resource;

public class Regex {
	
	private String str;
	private String type;
	private int lgstAcLen;
	
	public Regex(String str,String type){
		this.str = str;
		this.type = type;
		this.lgstAcLen = -1;
	}
	public void setLgstAcLen(int len){
		this.lgstAcLen = len;
	}
	
	public int getlgstAclen(){
		return this.lgstAcLen;		
	}
	public String getString(){
		return this.str;
	}
	public String getType(){
		return this.type;
	}
}
