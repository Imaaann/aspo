package com.aspodev.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;
import com.aspodev.SCAR.Dependency;
import com.aspodev.SCAR.Method;
import com.aspodev.TypeParser.TypeParser;
import com.aspodev.TypeParser.TypeSpace;
import com.aspodev.TypeParser.TypeToken;
import com.aspodev.parser.Scope.Scope;
import com.aspodev.parser.Scope.ScopeEnum;

public class ParserContext {
	private TypeSpace space;
	private TypeParser parser;
	private Scope scope;
	private Model model;
	private Stack<Slice> slices;

	private String pkgName = "NONE";
	private List<String> staticFunctions;
	private List<String> staticClasses;

	private Method currentMethod;
	private LocalVariableMap localVariables;

	private long instructionNumber;
	private Set<String> calledMethods;

	public ParserContext(TypeParser parser, Model model) {
		this.model = model;
		this.parser = parser;
		this.space = new TypeSpace();
		this.scope = new Scope();
		this.localVariables = new LocalVariableMap();
		this.slices = new Stack<>();
		this.staticFunctions = new ArrayList<>();
		this.staticClasses = new ArrayList<>();
		instructionNumber = 0;
		this.calledMethods = new HashSet<>();
	}

	// #region Scope methods

	public void changeScope(ScopeEnum newScope) {
		scope.changeScope(newScope);
	}

	/**
	 * Rewinds the scope back to the previous state, also destroys any
	 * localVariables found in that scope
	 */
	public void rewindScope() {
		localVariables.removeScope(getScopeCount());
		scope.rewindScope();
	}

	public int getScopeCount() {
		return scope.getScopeCount();
	}

	public ScopeEnum getCurrentScope() {
		return scope.getCurrentScope();
	}

	// #endregion

	// #region TypeSpace methods

	public void addType(TypeToken type) {
		this.space.addType(type);
	}

	public void addPackage(String pkgName) {
		this.space.addPackage(pkgName, parser);
	}

	public void addWildCardPackage(String pkgName) {
		this.space.addWildCardPackage(pkgName, parser);
	}

	public TypeToken getTypeToken(String typeName) {
		return this.space.getTypeToken(typeName);
	}

	public boolean isType(Token token) {
		return space.isType(token);
	}

	public String toString() {
		return space.toString();
	}

	// #endregion

	// #region SCAR methods

	public void createSlice(TypeToken data) {
		if (slices.empty())
			slices.push(new Slice(data));
		else
			slices.push(new Slice(data, this.getSlice().getMetaData().getFullName()));
	}

	public Slice getSlice() {
		return slices.peek();
	}

	public void finalizeSlice() {
		if (slices.empty())
			return;

		Slice current = slices.pop();
		resolveTypes(current);
		current.setInstructionNumber(instructionNumber);
		current.setCallNumber(calledMethods.size());
		model.addSlice(current);
	}

	public void resolveTypes(Slice slice) {

		List<Dependency> dependencies = slice.getDependencies().stream()
				.filter(d -> d.getCallerType().equals("RESOLVE")).toList();
		Set<String> methodNames = slice.getMethods().stream().map(m -> m.getName()).collect(Collectors.toSet());

		for (Dependency dependency : dependencies) {

			// case when method comes from same class
			if (methodNames.contains(dependency.getName())) {
				dependency.setCallerType(slice.getMetaData().name());
				continue;
			}

			String staticClassName = isStaticImport(dependency.getName());
			if (staticClassName != null) {
				dependency.setCallerType(staticClassName);
				continue;
			}

			if (isAmbiguous(slice)) {
				String ambiguous = slice.getParentName();
				for (String staticClass : staticClasses) {
					String[] components = staticClass.split("\\.");
					int size = components.length;
					String className = components[size - 1];

					if (ambiguous == null) {
						ambiguous = className;
					} else {
						ambiguous = ambiguous + "|" + className;
					}

				}

				dependency.setCallerType(ambiguous);
				continue;

			} else {
				if (slice.getParentName() != null) {
					dependency.setCallerType(slice.getParentName());
					continue;
				} else if (staticClasses.size() == 1) {
					String[] components = staticClasses.get(0).split("\\.");
					int size = components.length;
					String className = components[size - 1];
					dependency.setCallerType(className);
					continue;
				}
			}

		}

	}

	public String getClassName() {
		return this.getSlice().getMetaData().name();
	}

	// #endregion

	// #region Other Methods

	public void addStaticMethod(String methodName) {
		this.staticFunctions.add(methodName);
	}

	public void addStaticClass(String className) {
		this.staticClasses.add(className);
	}

	public String getStaticClass(String methodName) {
		String[] components = staticFunctions.stream().filter(s -> s.endsWith(methodName)).collect(Collectors.joining())
				.split("\\.");

		if (components.length > 1) {
			return components[components.length - 2];
		}

		return null;
	}

	public void setPackage(String pkgName) {
		this.pkgName = pkgName;
	}

	public String getPackage() {
		return this.pkgName;
	}

	public void setMethod(Method method) {
		this.currentMethod = method;
	}

	public Method getCurrentMethod() {
		return this.currentMethod;
	}

	public void addDependency(String methodName, String callerType) {

		if (scope.getCurrentScope() == ScopeEnum.CLASS) {
			getSlice().addDependency(methodName, callerType);
			return;
		}

		if (this.currentMethod == null)
			return;

		this.currentMethod.addDependency(methodName, callerType);
	}

	public void addLocalVariable(String typeName, String varName) {
		this.localVariables.addVariable(getScopeCount(), typeName, varName);
	}

	public String getVariableType(String varName) {
		return this.localVariables.getVariableType(getScopeCount(), varName);
	}

	public void deleteScopeVariables() {
		localVariables.removeScope(getScopeCount());
	}

	public boolean isAmbiguous(Slice slice) {
		if (staticClasses.size() > 1)
			return true;
		if (staticClasses.size() == 1 && slice.getParentName() != null)
			return true;

		return false;
	}

	public String isStaticImport(String methodName) {
		for (String staticFunction : staticFunctions) {
			String[] components = staticFunction.split("\\.");
			int size = components.length;
			if (components[size - 1].equals(methodName))
				return components[size - 2];
		}
		return null;
	}

	public long getInstructionNumber() {
		return instructionNumber;
	}

	public void increaseInstruction() {
		instructionNumber++;
	}

	public Set<String> getCalledMethods() {
		return calledMethods;
	}

	public void addCalledMethods(Collection<String> coll) {
		calledMethods.addAll(coll);
	}

	public void addAttributeDependency(String varName) {
		currentMethod.addAttributeDependency(varName);
	}

	public boolean isAttribute(String varName) {
		Slice current = getSlice();
		return current.getAttributes().stream().map(a -> a.getName()).collect(Collectors.toSet()).contains(varName);
	}

	// #endregion
}
