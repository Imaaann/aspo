package com.aspodev.SCAR;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.aspodev.TypeParser.TypeToken;

public class Slice {

	private final TypeToken metaData;
	private String parentName;
	private final Set<String> interfaceNames;
	private final Set<String> permitsNames;
	private Accessors accessor;
	private final Set<Modifier> modifiers;

	private final String outerClassName;

	private final Set<Attribute> attributes;
	private final Set<Method> methods;
	private final Set<Dependency> dependencies;
	private final Set<String> handledExceptions;
	private final Set<String> thrownExceptions;

	private long instructionNumber;
	private long callNumber;

	public Slice(TypeToken metaData, String outerClassName) {
		this.metaData = metaData;
		this.outerClassName = outerClassName;
		this.interfaceNames = new HashSet<>();
		this.modifiers = new HashSet<>();
		this.attributes = new HashSet<>();
		this.methods = new HashSet<>();
		this.permitsNames = new HashSet<>();
		this.dependencies = new HashSet<>();
		this.handledExceptions = new HashSet<>();
		this.thrownExceptions = new HashSet<>();
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

	public Set<String> getPermitted() {
		return permitsNames;
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

	public long getInstructionNumber() {
		return instructionNumber;
	}

	public Set<String> getHandledExceptions() {
		return handledExceptions;
	}

	public Set<String> getThrownExceptions() {
		return thrownExceptions;
	}

	public long getCallNumber() {
		return callNumber;
	}

	public void setCallNumber(long callNumber) {
		this.callNumber = callNumber;
	}

	public void setInstructionNumber(long instructionNumber) {
		this.instructionNumber = instructionNumber;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public void addInterface(String interfaceName) {
		this.interfaceNames.add(interfaceName);
	}

	public void addInterface(List<String> interfaceName) {
		this.interfaceNames.addAll(interfaceName);
	}

	public void setAccessor(Accessors accessor) {
		this.accessor = accessor;
	}

	public void addModifier(String modifier) {
		this.modifiers.add(Modifier.convert(modifier));
	}

	public void addModifier(List<Modifier> modifiers) {
		this.modifiers.addAll(modifiers);
	}

	public void addPermits(List<String> permits) {
		this.permitsNames.addAll(permits);
	}

	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}

	public void addMethod(Method method) {
		this.methods.add(method);
	}

	public void addDependency(String methodName, String callerType) {
		dependencies.add(new Dependency(methodName, callerType));
	}

	public void addException(String exceptionType) {
		this.thrownExceptions.add(exceptionType);
	}

	public void addHandledException(String exceptionType) {
		this.handledExceptions.add(exceptionType);
	}

	public boolean isInnerClass() {
		return !outerClassName.equals("normal");
	}

	public String getOuterClass() {
		return outerClassName;
	}

	public Set<Dependency> getDependencies() {
		Set<Dependency> dependencies = new HashSet<>();
		dependencies.addAll(this.dependencies);
		for (Method method : methods) {
			Set<Dependency> methodDependencies = method.getDependencies();
			dependencies.addAll(methodDependencies);
		}
		return dependencies;
	}

	@Override
	public String toString() {
		String depString = dependencies.stream().map(Dependency::toString).collect(Collectors.joining(" "));

		return """
				Slice{
				  metaData=%s,
				  instructionNumber=%d,
				  callerCount:%d
				  outerClassName='%s',
				  parentName='%s',
				  interfaces=%s,
				  permitted=%s
				  accessor=%s,
				  modifiers=%s,
				  attributes=%s,
				  methods=%s,
				  dependencies={
				  	%s}
				}
				""".formatted(metaData, instructionNumber, callNumber, outerClassName, parentName, interfaceNames,
				permitsNames, accessor, modifiers, attributes, methods, depString);
	}
}
