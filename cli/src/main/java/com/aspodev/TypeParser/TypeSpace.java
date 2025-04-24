package com.aspodev.TypeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aspodev.utils.JsonTools;

public class TypeSpace {
	private Set<TypeToken> typeSpace;

	public TypeSpace() {
		this(TypeSpace.loadStandardTypes());
		System.out.println(this.typeSpace.size());
	}

	public TypeSpace(List<TypeToken> defaultTypes) {
		this.typeSpace = new HashSet<TypeToken>(256);
		this.typeSpace.addAll(defaultTypes);
	}

	public void addPackage(String pkg, TypeParser globalTypeSpace) {
		List<TypeToken> foundTypes = globalTypeSpace.findPackageTypes(pkg);
		typeSpace.addAll(foundTypes);
	}

	public void addType(String name, String pkg, TypeTokenEnum type) {
		typeSpace.add(new TypeToken(name, pkg, type));
	}

	public void addType(TypeToken typeToken) {
		typeSpace.add(typeToken);
	}

	public void addWildCardPackage(String pkg, TypeParser globalTypeSpace) {
	    try {
	        // Read the library JSON file
	        LibraryTypes libraryTypes = JsonTools.readJsonObject("/LibraryJSON.json", LibraryTypes.class);

	        // Filter and add types matching the wildcard package
	        libraryTypes.classes().stream()
	            .filter(className -> className.startsWith(pkg.replace(".*", "")))
	            .forEach(className -> typeSpace.add(new TypeToken(className, pkg, TypeTokenEnum.CLASS)));

	        libraryTypes.interfaces().stream()
	            .filter(interfaceName -> interfaceName.startsWith(pkg.replace(".*", "")))
	            .forEach(interfaceName -> typeSpace.add(new TypeToken(interfaceName, pkg, TypeTokenEnum.INTERFACE)));

	        libraryTypes.enums().stream()
	            .filter(enumName -> enumName.startsWith(pkg.replace(".*", "")))
	            .forEach(enumName -> typeSpace.add(new TypeToken(enumName, pkg, TypeTokenEnum.ENUM)));

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
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

	public String toString() {
		return typeSpace.toString();
	}
}
