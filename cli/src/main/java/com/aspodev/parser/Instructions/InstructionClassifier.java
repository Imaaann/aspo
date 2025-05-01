package com.aspodev.parser.Instructions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;


public class InstructionClassifier {
	private List<String> rawInstruction;
	private List<Token> classifiedTokens;
	private InstructionTypes instructionType;
	public InstructionClassifier(List<String> instruction) {
		this.rawInstruction = instruction;
		this.classifiedTokens = new ArrayList<>(16);
	}

	public Instruction classify(ParserContext context) {
		classifiedTokens = classifyTokens();
		instructionType = classifyInstruction(context);
		return new Instruction(classifiedTokens, instructionType);
	}

	private List<Token> classifyTokens() {
		return rawInstruction.stream().map(token -> new Token(token)).collect(Collectors.toList());
	}

	private InstructionTypes classifyInstruction(ParserContext context) {
		// TODO: make this function work ffs
		if (classifiedTokens.contains(new Token("import")))
			return InstructionTypes.IMPORT_STATEMENT;
			
		if (classifiedTokens.contains(new Token("package")))
	
			return InstructionTypes.PACKAGE_STATEMENT;
		if(isAttribute(context))
			return InstructionTypes.ATTRIBUTE_DECLARATION;

		
		return InstructionTypes.OTHER;
	}
	//TO discuss
	private boolean isAttribute(ParserContext context){
		//TODO add scope condition
		Iterator<Token> iterator = classifiedTokens.iterator();
		int idtcount = 0;
		while(iterator.hasNext() && idtcount == 0){
			Token token = iterator.next();
			if(token.isIdentifier() && iterator.hasNext()){
				idtcount++;
				Token temp = iterator.next();
				if (temp.equals('<')){
					token.append(temp);
					do{
						temp=iterator.next();
						token.append(temp);
					}while(!temp.equals('>'));
					if(iterator.hasNext()){
						token = iterator.next();
					}
				}
				
					return token.isIdentifier();
				
			}
		}
		return false; 
	}
}
