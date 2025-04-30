package com.aspodev.parser.Instructions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.aspodev.parser.ParserConstants;

public class InstructionBuilder {
	private Iterator<String> iterator;
	private List<String> instruction;

	public InstructionBuilder(Iterator<String> iterator) {
		this.iterator = iterator;
		this.instruction = new ArrayList<>(32);
	}

	public void build() {
		while (iterator.hasNext()) {
			String token = iterator.next();

			if (token.equals(";") || token.equals("}") || token.equals("{") || token.equals(":")) {
				instruction.add(token);
				break;
			}

			instruction.add(token);
		}
	}

	public void clean() {
		instruction = instruction.stream().filter((token) -> !ParserConstants.skippableTokens.contains(token))
				.collect(Collectors.toList());
	}

	public List<String> getInstruction() {
		return this.instruction;
	}

	public String toString() {
		return instruction.toString();
	}
}
