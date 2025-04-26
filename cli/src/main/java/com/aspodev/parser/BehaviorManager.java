package com.aspodev.parser;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
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
		registry.put(ParserBehaviors.SKIP, (c) -> {
		});

		// Behavior: add type to typeSpace
		registry.put(ParserBehaviors.IMPORT_STATEMENT, (c) -> {
			String identifier = c.getIdentifier();
			List<String> components = Arrays.asList(identifier.split("\\."));
			String typeName = components.get(components.size() - 1);
			String pkgName = components.stream().limit(components.size() - 1).collect(Collectors.joining("."));

			System.out.println(new TypeToken(typeName, pkgName, TypeTokenEnum.IMPORTED));
			c.addType(typeName, pkgName, TypeTokenEnum.IMPORTED);
		});

	}

	public static BehaviorManager getInstance() {
		return Holder.instance;
	}

	public void execute(ParserBehaviors behavior, ParserContext context) {
		registry.getOrDefault(behavior, c -> {
		}).apply(context);
	}

}
