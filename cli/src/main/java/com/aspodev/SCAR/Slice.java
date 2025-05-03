package com.aspodev.SCAR;

import java.util.HashSet;
import java.util.Set;

import com.aspodev.TypeParser.TypeToken;

public class Slice {

	private final TypeToken metaData;
	private String parentName;
	private final Set<String> interfaceNames;
	private Accessors accessor;
	private final Set<Modifier> modifiers;

	private final String outerClassName;

	private final Set<Attribute> attributes;
	private final Set<Method> methods;

	public Slice(TypeToken metaData, String outerClassName) {
		this.metaData = metaData;
		this.outerClassName = outerClassName;
		this.interfaceNames = new HashSet<>();
		this.modifiers = new HashSet<>();
		this.attributes = new HashSet<>();
		this.methods = new HashSet<>();
	}

	public Slice(TypeToken metaData) {
		this(metaData, "normal");
	}

	public TypeToken getMetaData() {
		return metaData;
	}

	public String getParentName() {
		return parentName;
	}

	public Set<String> getInterfaces() {
		return interfaceNames;
	}

	public Accessors getAccessor() {
		return accessor;
	}

	public Set<Modifier> getModifiers() {
		return modifiers;
	}

	public Set<Attribute> getAttributes() {
		return attributes;
	}

	public Set<Method> getMethods() {
		return methods;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public void addInterface(String interfaceName) {
		this.interfaceNames.add(interfaceName);
	}

	public void setAccessor(String accessor) {
		this.accessor = Accessors.convert(accessor);
	}

	public void addModifier(String modifier) {
		this.modifiers.add(Modifier.convert(modifier));
	}

	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}

	public void addMethod(Method method) {
		this.methods.add(method);
	}

	public boolean isInnerClass() {
		return !outerClassName.equals("normal");
	}

	public String getOuterClass() {
		return outerClassName;
	}

}
