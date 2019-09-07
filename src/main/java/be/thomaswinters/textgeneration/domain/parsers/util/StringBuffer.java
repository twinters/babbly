package be.thomaswinters.textgeneration.domain.parsers.util;

public class StringBuffer {
	private StringBuilder builder = new StringBuilder();
	
	public void append(char character) {
		builder.append(character);
	}
	
	public void append(CharSequence string) {
		builder.append(string);
	}
	
	public boolean isEmpty() {
		return builder.toString().equals("");
	}
	
	public String poll() {
		String result = builder.toString();
		builder = new StringBuilder();
		return result;
	}
	
	@Override
	public String toString() {
		return "BUFFER: <" + builder.toString() + ">";
	}
}
