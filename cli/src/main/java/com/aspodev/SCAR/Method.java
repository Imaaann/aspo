package com.aspodev.SCAR;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Method {
	private final String name;
	private final String returnType;
	private final Accessors accessor;
	private final List<String> arguments;
	private final Set<Modifier> modifiers;
	private final Set<Dependency> dependencies;

	public Method(String name, String returnType, String accessor) {
		this.name = name;
		this.returnType = returnType;
		this.accessor = Accessors.convert(accessor);
		this.modifiers = new HashSet<>();
		this.arguments = new ArrayList<>();
		this.dependencies = new HashSet<>();
	}

	public void addModifier(String modifier) {
		this.modifiers.add(Modifier.convert(modifier));
	}

	public void addArgument(String typeName) {
		this.arguments.add(typeName);
	}

	public void addDependency(String methodName, String callerType) {
		this.dependencies.add(new Dependency(methodName, callerType));
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.returnType;
	}

	public Accessors getAccessor() {
		return this.accessor;
	}

	public Set<Modifier> getModifiers() {
		return this.modifiers;
	}

}
