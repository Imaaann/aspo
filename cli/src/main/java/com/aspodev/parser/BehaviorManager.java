package com.aspodev.parser;

import java.util.EnumMap;
import java.util.Map;

public class BehaviorManager {
	private final Map<ParserBehaviors, Behavior> registry = new EnumMap<>(ParserBehaviors.class);

	private static class Holder {
		private static final BehaviorManager instance = new BehaviorManager();
	}

	private BehaviorManager() {
		// TODO: register all methods

		// Behavior: Skip current token
		registry.put(ParserBehaviors.SKIP, (c) -> {
			c.skip();
		});

	}

	public static BehaviorManager getInstance() {
		return Holder.instance;
	}

	public void execute(ParserBehaviors behavior, ParserContext context) {
		registry.getOrDefault(behavior, c -> c.skip()).apply(context);
	}

}
