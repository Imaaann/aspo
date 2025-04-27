package com.aspodev.tokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.aspodev.cleaner.Cleaner;
import com.aspodev.utils.RegexTools;

public class Tokenizer {
	private StringBuilder contents;
	private List<String> tokens;

	public Tokenizer(Path path) {
		this.tokens = new ArrayList<>();
		try {
			this.contents = new StringBuilder(Files.readString(path));
			Cleaner.cleanFile(contents);
			System.out.println("[DEBUG] == Cleanfile (inside tokenizer)");
			System.out.println(contents);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void tokenize() {
		splitAcrossWhiteSpace();

		List<String> OPERATORS = Arrays.asList(">>>=", ">>>", "<<=", ">>=", "...", "->", "==", ">=", "<=", "!=", "&&",
				"||", "++", "--", "<<", ">>", "+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=", "::", "=", ">", "<", "!",
				"~", "?", ":", "+", "-", "*", "/", "&", "|", "^", "%", "(", ")", "{", "}", "[", "]", ";", ",", ".", "@",
				"\"\"\"", "\"", "\'");

		String OperatorsPattern = OPERATORS.stream().sorted((a, b) -> Integer.compare(b.length(), a.length()))
				.map(Pattern::quote).collect(Collectors.joining("|"));

		String fullRegex = OperatorsPattern;

		this.tokens = this.splitAroundRegex(fullRegex);
	}

	public List<String> getTokens() {
		return tokens;
	}

	private List<String> splitAroundRegex(String regex) {
		List<String> resultList = new ArrayList<>();

		for (String token : this.tokens) {
			if (RegexTools.stringContainsRegex(token, regex)) {
				List<String> splitTokens = RegexTools.splitAround(token, regex);
				resultList.addAll(splitTokens);
			} else {
				resultList.add(token);
			}
		}

		return resultList;
	}

	public String toString() {
		String result = "";
		for (String token : tokens) {
			result = result + token + "\n";
		}
		return result;
	}

	private void splitAcrossWhiteSpace() {
		this.tokens = RegexTools.splitAcross(contents.toString(), "\\s+");
	}

}
