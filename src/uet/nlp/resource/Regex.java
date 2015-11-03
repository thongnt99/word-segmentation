package uet.nlp.resource;

public class Regex {

	public static String PHRASE_TYPE = "phrase";

	private String str;
	private String type;
	private int lgstAcLen;

	public Regex(String str, String type) {
		this.str = str;
		this.type = type;
		this.lgstAcLen = -1;
	}

	public void setLgstAcLen(int len) {
		this.lgstAcLen = len;
	}

	public int getlgstAclen() {
		return this.lgstAcLen;
	}

	public String getString() {
		return this.str;
	}

	public String getType() {
		return this.type;
	}

	public boolean isPhrase() {
		return type.equals(PHRASE_TYPE);
	}
	public boolean isName(){
		return type.startsWith("name");
	}
	public boolean isAllCap(){
		return type.startsWith("allcaps");
	}
}
