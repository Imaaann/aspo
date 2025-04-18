package com.aspodev.tokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.aspodev.cleaner.Cleaner;
import com.aspodev.utils.RegexTools;

public class Tokenizer {
	private StringBuilder contents;
	private List<String> tokens;
	private HashMap<String, Boolean> CLASS_TREE;// <Name of the class,has extended before or no>
	private static HashMap<String,String> EXTENSIONS;

	private int index = 0;

	public Tokenizer(Path path) {
		this.tokens = new ArrayList<>();
		this.CLASS_TREE = new HashMap<>();
		EXTENSIONS = new HashMap<>();
		try {
			this.contents = new StringBuilder(Files.readString(path));
			Cleaner.cleanFile(contents);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, String> getEXTENSIONS() {
		return EXTENSIONS;
	}

	public HashMap<String, Boolean> getCLASS_TREE() {
		return this.CLASS_TREE;
	}
// if contents have all tokens on this file
//	public void extensionsMap() {
//		for (String content : contents.toString().split("\n")) {
//			if (content.equals("extends")) {
//				EXTENSIONS.put(content.before,content.after);
//			}
//		}
//	}

	private static void processFile(Path file) {
		try {
			// Read file content
			String content = new String(Files.readAllBytes(file));

			// Regex to find classes with "extends"
			Pattern pattern = Pattern.compile("class\\s+(\\w+)\\s+extends\\s+(\\w+)");
			Matcher matcher = pattern.matcher(content);

			while (matcher.find()) {
				String className = matcher.group(1);   // Get class name
				String parentClass = matcher.group(2); // Get parent class

				// Add to the EXTENSIONS map
				EXTENSIONS.put(className, parentClass);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void class_Tree() {
		for(String key : EXTENSIONS.keySet()) {
			if(EXTENSIONS.get(key) != null) {
				CLASS_TREE.put(EXTENSIONS.get(key),true);
				CLASS_TREE.put(key,false);
			}
		}
	}

	public void tokenize() {
		splitAcrossWhiteSpace();

		String STRING_LITTERAL_PATTERN = "\\\"(?:\\\\.|[^\\\"\\\\])*\\\"";
		String HEX_FLOAT_PATTERN = "(?:0[xX](?:(?:[0-9A-Fa-f](?:_*[0-9A-Fa-f])*)(?:\\.(?:[0-9A-Fa-f](?:_*[0-9A-Fa-f])*)?)?|\\.(?:[0-9A-Fa-f](?:_*[0-9A-Fa-f])*))[pP][+-]?[0-9](?:_*[0-9])*[fFdD]?)";
		String DEC_FLOAT_PATTERN = "(?:[0-9](?:_*[0-9])*\\.(?:[0-9](?:_*[0-9])*)?(?:[eE][+-]?[0-9](?:_*[0-9])*)?[fFdD]?|\\.[0-9](?:_*[0-9])*(?:[eE][+-]?[0-9](?:_*[0-9])*)?[fFdD]?|[0-9](?:_*[0-9])*(?:[eE][+-]?[0-9](?:_*[0-9])*)(?:[fFdD])?|[0-9](?:_*[0-9])*(?:[eE][+-]?[0-9](?:_*[0-9])*)?[fFdD])";
		String INTEGER_PATTERN = "(?:0[xX][0-9A-Fa-f](?:_*[0-9A-Fa-f])*|0[bB][01](?:_*[01])*|0[0-7](?:_*[0-7])*|[1-9](?:_*[0-9])*)[lL]?";

		List<String> OPERATORS = Arrays.asList(">>>=", ">>>", "<<=", ">>=", "...", "->", "==", ">=", "<=", "!=", "&&",
				"||", "++", "--", "<<", ">>", "+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=", "::", "=", ">", "<", "!",
				"~", "?", ":", "+", "-", "*", "/", "&", "|", "^", "%", "(", ")", "{", "}", "[", "]", ";", ",", ".",
				"@");

		String OperatorsPattern = OPERATORS.stream().sorted((a, b) -> Integer.compare(b.length(), a.length()))
				.map(Pattern::quote).collect(Collectors.joining("|"));

		String fullRegex = STRING_LITTERAL_PATTERN + "|" + HEX_FLOAT_PATTERN + "|" + DEC_FLOAT_PATTERN + "|"
				+ INTEGER_PATTERN + "|" + OperatorsPattern;

		this.tokens = this.splitAroundRegex(fullRegex);
	}

	public int getTokenNumber() {
		return this.tokens.size();
	}

	public String getNextToken() {
		String token = index < getTokenNumber() ? tokens.get(index) : "";
		index++;
		return token;
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

	private void FILLING_CLASS_TREE(HashMap<String,String> extensions) {

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
