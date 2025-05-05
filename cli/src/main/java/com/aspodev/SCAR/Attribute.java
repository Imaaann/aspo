package com.aspodev.SCAR;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Attribute {
	private final String name;
	private final String typeName;
	private final Accessors accessor;
	private final Set<Modifier> modifiers;

	public Attribute(String name, String typeName, Accessors accessor) {
		this.name = name;
		this.typeName = typeName;
		this.accessor = accessor;
		this.modifiers = new HashSet<>();
	}

	public void addModifier(String modifier) {
		this.modifiers.add(Modifier.convert(modifier));
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.typeName;
	}

	public Accessors getAccessor() {
		return this.accessor;
	}

	public Set<Modifier> getModifiers() {
		return this.modifiers;
	}

	@Override
	public String toString() {
		String mods = modifiers.stream().map(Modifier::toString).collect(Collectors.joining(" "));
		String access = accessor != null ? accessor.toString().toLowerCase() : "";
		String prefix = Stream.of(access, mods).filter(s -> !s.isBlank()).collect(Collectors.joining(" "));
		return "\n" + (prefix.isBlank() ? "" : prefix + " ") + typeName + " " + name;
	}

}