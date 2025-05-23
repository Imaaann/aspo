package com.aspodev.TypeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aspodev.parser.Token;
import com.aspodev.utils.JsonTools;
import com.fasterxml.jackson.core.type.TypeReference;

public class TypeSpace {
	private Set<TypeToken> typeSpace;
	private static final List<TypeToken> defaultTypes = TypeSpace.loadStandardTypes();

	public TypeSpace() {
		this.typeSpace = new HashSet<TypeToken>(256);
		this.typeSpace.addAll(defaultTypes);
	}

	public void addPackage(String pkg, TypeParser globalTypeSpace) {
		;
		List<TypeToken> foundTypes = globalTypeSpace.findPackageTypes(pkg);
		typeSpace.addAll(foundTypes);
	}

	public void addType(String name, String pkg, TypeTokenEnum type) {
		typeSpace.add(new TypeToken(name, pkg, type));
	}

	public void addType(TypeToken typeToken) {
		typeSpace.add(typeToken);
	}

	public TypeToken getTypeToken(String typeName) {
		for (TypeToken token : typeSpace) {
			if (token.name().equals(typeName))
				return token;
		}
		return null;
	}

	public void addWildCardPackage(String pkg, TypeParser globalTypeSpace) {
		String basePkg = pkg.replace(".*", "");
		String jsonResourcePath = "/Library.json";

		try {

			// Check if package is from the java library
			if (!basePkg.startsWith("java") && !basePkg.startsWith("javax")) {
				addPackage(basePkg, globalTypeSpace);
				return;
			}

			// 1) Read the entire root as a Map<packageName, LibraryTypes>
			TypeReference<Map<String, LibraryTypes>> typeRef = new TypeReference<Map<String, LibraryTypes>>() {
			};

			Map<String, LibraryTypes> allPackages = JsonTools.readJsonObject(jsonResourcePath, typeRef);

			// 2) For each entry whose key startsWith our basePkg, extract and add
			allPackages.entrySet().stream().filter(entry -> entry.getKey().startsWith(basePkg)).forEach(entry -> {
				LibraryTypes lib = entry.getValue();

				lib.classes().forEach(className -> this.addType(className, basePkg, TypeTokenEnum.CLASS));

				lib.interfaces()
						.forEach(interfaceName -> this.addType(interfaceName, basePkg, TypeTokenEnum.INTERFACE));

				lib.enums().forEach(enumName -> this.addType(enumName, basePkg, TypeTokenEnum.ENUM));
			});

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

	public boolean isType(Token token) {
		return typeSpace.contains(new TypeToken(token.getValue(), null, null));
	}
}
