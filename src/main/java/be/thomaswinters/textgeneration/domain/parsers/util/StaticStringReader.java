package be.thomaswinters.textgeneration.domain.parsers.util;

/**
 * Use this reader to read from a static string one character at a time.
 * 
 * @author Thomas Winters
 *
 */
public class StaticStringReader {
	private final String string;
	private int index = 0;

	public StaticStringReader(String string) {
		this.string = string;
	}

	public boolean hasNext() {
		return index < string.length();
	}

	public char peek() {
		return string.charAt(index);
	}

	public char next() {
		return string.charAt(index++);
	}

	public String getWholeString() {
		return string;
	}
}
