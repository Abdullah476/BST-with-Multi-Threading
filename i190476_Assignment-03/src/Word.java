
public class Word {
	private String word;
	private int frequency;
	
	public Word(){
		word = "";
		frequency = 0;
	}
	
	public Word(String word) {
		this.word = word;
		this.frequency = 1;
	}
	
	public String getWord() {
		return word;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public void updateFrequency() {
		frequency++;
	}
}
