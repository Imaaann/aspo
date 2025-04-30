package com.aspodev.parser.Instructions;

import java.util.Iterator;
import java.util.List;

public class InstructionBuilder {
	private Iterator<String> iterator;
	private List<String> instruction;

	public InstructionBuilder(Iterator<String> iterator) {
		this.iterator = iterator;

	}

	public void build() {
		while (iterator.hasNext()) {
			String token = iterator.next();

			if (token.equals(";") || token.equals("}") || token.equals("{")) {
				instruction.add(token);
				break;
			}

			instruction.add(token);
		}
	}
}
