package com.aspodev.SCAR;

import java.util.HashSet;
import java.util.Set;

public class Attribute {
	private final String name;
	private final String typeName;
	private final Accessors accessor;
	private final Set<Modifier> modifiers;

	public Attribute(String name, String typeName, String accessor) {
		this.name = name;
		this.typeName = typeName;
		this.accessor = Accessors.convert(accessor);
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
}