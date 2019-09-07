package be.thomaswinters.textgeneration.domain.util;

import com.google.common.base.Charsets;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class StringConverter {
	public static void convertLowercase(List<String> strings) {
		for (int i = 0; i < strings.size(); i++) {
			strings.set(i, strings.get(i).toLowerCase());
		}
	}

	public static void trim(List<String> strings) {
		for (int i = 0; i < strings.size(); i++) {
			strings.set(i, strings.get(i).trim());
		}
	}

	public static void capitalize(List<String> strings) {
		for (int i = 0; i < strings.size(); i++) {
			strings.set(i, capitalize(strings.get(i)));
		}
	}

	public static String capitalize(String string) {
		return WordUtils.capitalize(string);
	}

	public static void removeDuplicates(List<String> strings) {
		Set<String> set = new LinkedHashSet<String>(strings);
		strings.clear();
		strings.addAll(set);
	}

	public static void sortAlphabetically(List<String> strings) {
		Collections.sort(strings, String.CASE_INSENSITIVE_ORDER);
	}

	public static List<String> readLines(File file) throws IOException {
		try {
			return Files.readAllLines(file.toPath(), Charsets.UTF_8);
		} catch (MalformedInputException e) {
			return Files.readAllLines(file.toPath(), Charsets.ISO_8859_1);
		}
	}

	public static void writeLines(List<String> string, File file)
			throws IOException {
		Files.write(file.toPath(),
				string, Charsets.UTF_8);
	}

	public static void main(String[] args) throws IOException {
		File file = new File("");

		List<String> lines = readLines(file);
		trim(lines);
		// convertLowercase(lines);
		removeDuplicates(lines);
		// capitalize(lines);
		sortAlphabetically(lines);
		writeLines(lines, file);
	}
}
