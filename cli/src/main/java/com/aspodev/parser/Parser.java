package com.aspodev.parser;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import com.aspodev.SCAR.Model;
import com.aspodev.TypeParser.TypeParser;
import com.aspodev.parser.Behavior.BehaviorManager;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionBuilder;
import com.aspodev.parser.Instructions.InstructionClassifier;
import com.aspodev.tokenizer.Tokenizer;

public class Parser {
	private List<String> tokens;
	private Iterator<String> iterator;
	private ParserContext context;

	public Parser(Path path, TypeParser parser, Model model) {
		Tokenizer tokenizer = new Tokenizer(path);
		tokenizer.tokenize();

		this.tokens = tokenizer.getTokens();
		this.iterator = tokens.iterator();

		this.context = new ParserContext(parser, model);
	}

	/**
	 * parse the current file and add the slices through behaviors
	 */
	public void parse() {
		// Main parsing loop
		BehaviorManager manager = BehaviorManager.getInstance();

		while (iterator.hasNext()) {
			// Build an instruction
			InstructionBuilder builder = new InstructionBuilder(iterator);
			builder.build();
			builder.clean();
			List<String> rawInstruction = builder.getInstruction();

			// Classify the tokens and instruction
			InstructionClassifier classifier = new InstructionClassifier(rawInstruction);
			Instruction instruction = classifier.classify(this.context);

			// Execute behavior assosiated with the instruction
			manager.execute(this.context, instruction);
		}

		System.out.println("[DEBUG] Slice created: " + context.getSlice());
	}
}
