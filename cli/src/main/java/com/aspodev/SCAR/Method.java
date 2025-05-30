package com.aspodev.SCAR;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Method {
	private final String name;
	private final String returnType;
	private final Accessors accessor;
	private final String genericHeader;
	private final List<String> arguments;
	private final Set<Modifier> modifiers;
	private final Set<Dependency> dependencies;
	private final Set<String> attributeDependencies;

	public Method(String name, String returnType, Accessors accessor, String genericHeader) {
		this.name = name;
		this.returnType = returnType;
		this.accessor = accessor;
		this.genericHeader = genericHeader == null ? "none" : genericHeader;
		this.modifiers = new HashSet<>();
		this.arguments = new ArrayList<>();
		this.dependencies = new HashSet<>();
		this.attributeDependencies = new HashSet<>();
	}

	public void addModifier(String modifier) {
		this.modifiers.add(Modifier.convert(modifier));
	}

	public void addModifier(List<Modifier> modifiers) {
		this.modifiers.addAll(modifiers);
	}

	public void addArgument(String typeName) {
		this.arguments.add(typeName);
	}

	public void addArgument(Collection<String> types) {
		this.arguments.addAll(types);
	}

	public void addDependency(String methodName, String callerType) {
		this.dependencies.add(new Dependency(methodName, callerType));
	}

	public void addAttributeDependency(String attributeName) {
		this.attributeDependencies.add(attributeName);
	}

	public Set<Dependency> getDependencies() {
		return dependencies;
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

	public List<String> getArguments() {
		return this.arguments;
	}

	public Set<Modifier> getModifiers() {
		return this.modifiers;
	}

	public Set<String> getAttributeDependencies() {
		return attributeDependencies;
	}

	public String getGenericHeader() {
		return this.genericHeader;
	}

	public boolean isGenericMethod() {
		return genericHeader.equals("none");
	}

	@Override
	public String toString() {
		String access = accessor != null ? accessor.toString().toLowerCase() : "";
		String mods = modifiers.stream().map(Modifier::toString).collect(Collectors.joining(" "));
		String generics = isGenericMethod() ? "" : genericHeader + " ";
		String args = String.join(", ", arguments);
		String prefix = Stream.of(access, mods).filter(s -> !s.isBlank()).collect(Collectors.joining(" "));
		String dependenciesString = dependencies.stream().map(Dependency::toString).collect(Collectors.joining(" "));
		return "\n\t\t" + (prefix.isBlank() ? "" : prefix + " ") + generics + returnType + " " + name + "(" + args + ")"
				+ "{\n" + dependenciesString + "\t\t}" + "\t\t\nAttribute Dependencies: " + attributeDependencies
				+ "\n";
	}

}
