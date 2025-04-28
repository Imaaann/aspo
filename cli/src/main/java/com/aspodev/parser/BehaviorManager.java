package com.aspodev.parser;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aspodev.TypeParser.TypeToken;
import com.aspodev.TypeParser.TypeTokenEnum;

public class BehaviorManager {
	private final Map<ParserBehaviors, Behavior> registry = new EnumMap<>(ParserBehaviors.class);

	private static class Holder {
		private static final BehaviorManager instance = new BehaviorManager();
	}

	private BehaviorManager() {
		// TODO: register all methods

		// Behavior: Skip current token
		registry.put(ParserBehaviors.SKIP, (c, t) -> {
		});

		// Behavior: add type to typeSpace
		registry.put(ParserBehaviors.IMPORT_STATEMENT, (c, t) -> {
			String identifier = c.getIdentifier();
			List<String> components = Arrays.asList(identifier.split("\\."));
			String typeName = components.get(components.size() - 1);
			String pkgName = components.stream().limit(components.size() - 1).collect(Collectors.joining("."));

			System.out.println("[DEBUG] == found import: " + new TypeToken(typeName, pkgName, TypeTokenEnum.IMPORTED));
			c.addType(typeName, pkgName, TypeTokenEnum.IMPORTED);
		});

		// Behavior: add package to typeSpace
		registry.put(ParserBehaviors.PACKAGE_STATEMENT, (c, t) -> {
			String identifier = c.getIdentifier();
			c.addPackage(identifier);
		});

		// Behavior: add modifier to list
		registry.put(ParserBehaviors.MODIFIER, (c, t) -> {
			c.addModifier(t);
		});

		// Behavior: set current accessor (ther can't be multiple)
		registry.put(ParserBehaviors.ACCESSOR, (c, t) -> {
			c.setAccessor(t);
		});

		// Behavior: skip string literals
		registry.put(ParserBehaviors.STRING_LITERAL, (c, t) -> {
			Iterator<String> it = c.getIterator();
			boolean escapedCharacter = false;
			String token = "";
			String dbSkippedLiteral = t;

			while (it.hasNext()) {
				token = it.next();
				dbSkippedLiteral += token;

				if (escapedCharacter) {
					escapedCharacter = false;
					continue;
				}

				if (token.equals("\\")) {
					escapedCharacter = true;
					continue;
				}

				if (token.equals("\"")) {
					break;
				}

				if (token.equals("\"\"\"")) {
					break;
				}
			}

			System.out.println("[DEBUG] == skipped literal: " + dbSkippedLiteral);
		});

	}

	public static BehaviorManager getInstance() {
		return Holder.instance;
	}

	public void execute(ParserBehaviors behavior, ParserContext context, String token) {
		registry.getOrDefault(behavior, (c, t) -> {
		}).apply(context, token);
	}

}
