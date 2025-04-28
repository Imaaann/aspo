package com.aspodev.parser;

@FunctionalInterface
public interface Behavior {
	public abstract void apply(ParserContext context, String token);
}
