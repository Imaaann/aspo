package com.aspodev.parser.Behavior;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aspodev.SCAR.Accessors;
import com.aspodev.SCAR.Modifier;
import com.aspodev.SCAR.Slice;
import com.aspodev.SCAR.Attribute;
import com.aspodev.SCAR.Method;
import com.aspodev.TypeParser.TypeToken;
import com.aspodev.TypeParser.TypeTokenEnum;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionUtil;
import com.aspodev.parser.Scope.ScopeEnum;

public class RecordBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		List<Modifier> modifiers = instruction.getModifiers();
		Accessors accessor = instruction.getAccessor();
		int recordTokenPos = instruction.getToken("record").getPosition();
		Token recordName = instruction.getToken(recordTokenPos + 1);
		List<Token> interfaceList = instruction.getInterfaceNames();

		Iterator<Token> iterator = instruction.getTokens().iterator();

		Token temp = new Token("");
		while (iterator.hasNext() && !temp.getValue().equals("(")) {
			temp = iterator.next();
		}

		Map<String, String> headerTypes = InstructionUtil.getParameterList(iterator, temp);

		context.createSlice(new TypeToken(recordName.getValue(), context.getPackage(), TypeTokenEnum.RECORD));

		Slice recordSlice = context.getSlice();

		recordSlice.setAccessor(accessor);
		recordSlice.addModifier(modifiers);

		if (interfaceList != null)
			recordSlice.addInterface(interfaceList.stream().map(t -> t.getValue()).toList());

		context.changeScope(ScopeEnum.CLASS);
		context.addLocalVariable(recordName.getValue(), "this");

		for (String varNames : headerTypes.keySet()) {
			String typeName = headerTypes.get(varNames);
			Attribute headerAttr = new Attribute(varNames, typeName, Accessors.PRIVATE);
			headerAttr.addModifier("final");

			recordSlice.addAttribute(headerAttr);
			context.addLocalVariable(typeName, varNames);

			Method method = new Method(varNames, typeName, Accessors.PUBLIC, "none");
			recordSlice.addMethod(method);
		}

	}

}
