package com.aspodev.TypeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aspodev.utils.JsonTools;

public class TypeSpace {
	private Set<TypeToken> typeSpace;
	private static final List<TypeToken> defaultTypes = TypeSpace.loadStandardTypes();

	public TypeSpace() {
		this.typeSpace = new HashSet<TypeToken>(256);
		this.typeSpace.addAll(defaultTypes);
	}

	public void addPackage(String pkg, TypeParser globalTypeSpace) {
		List<TypeToken> foundTypes = globalTypeSpace.findPackageTypes(pkg);
		System.out.println("[DEBUG] added types from pkg(" + pkg + "):" + foundTypes);
		typeSpace.addAll(foundTypes);
	}

	public void addType(String name, String pkg, TypeTokenEnum type) {
		typeSpace.add(new TypeToken(name, pkg, type));
	}

	public void addType(TypeToken typeToken) {
		typeSpace.add(typeToken);
	}

	public void addWildCardPackage(String pkg, TypeParser globalTypeSpace) {
		// TODO: Read from the LibraryJSON file to resolve wild card packages
	}

	public static List<TypeToken> loadStandardTypes() {
		List<TypeToken> standardList = new ArrayList<>();

		try {
			LibraryTypes StandardTypes = JsonTools.readJsonObject("/StandardTypes.json", LibraryTypes.class);

			StandardTypes.classes().forEach(
					(className) -> standardList.add(new TypeToken(className, "java.lang", TypeTokenEnum.CLASS)));

			StandardTypes.interfaces().forEach(
					(className) -> standardList.add(new TypeToken(className, "java.lang", TypeTokenEnum.INTERFACE)));

			StandardTypes.enums().forEach(
					(className) -> standardList.add(new TypeToken(className, "java.lang", TypeTokenEnum.ENUM)));

		} catch (IOException e) {
			e.printStackTrace();
		}
		List<String> primitiveList = List.of("byte", "short", "int", "long", "float", "double", "char", "boolean");

		primitiveList.forEach(
				(primitive) -> standardList.add(new TypeToken(primitive, "special.primitve", TypeTokenEnum.PRIMITVE)));

		return standardList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TypeSpace [").append(typeSpace.size()).append(" entries]\n");
		sb.append("{\n");

		typeSpace.stream().sorted(Comparator.comparing(TypeToken::pkg))
				.forEach(t -> sb.append(String.format("  - %-40s pkg=%-25s kind=%s%n", t.name(), t.pkg(), t.type())));

		sb.append("}");
		return sb.toString();
	}

}
