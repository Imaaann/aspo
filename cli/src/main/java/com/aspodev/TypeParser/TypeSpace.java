package com.aspodev.TypeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aspodev.utils.JsonTools;

public class TypeSpace {
	private ArrayList<TypeToken> typeSpace;

	public TypeSpace() {
		this.typeSpace = new ArrayList<>();
		this.loadStandardTypes();
	}

	public void addPackage(String pkg, TypeParser globalTypeSpace) {
		List<TypeToken> foundTypes = globalTypeSpace.findPackageTypes(pkg);
		typeSpace.addAll(foundTypes);
		this.loadStandardTypes();
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

	private void loadStandardTypes() {
		try {
			LibraryTypes StandardTypes = JsonTools.readJsonObject("/StandardTypes.json", LibraryTypes.class);

			StandardTypes.classes().forEach((className) -> this.addType(className, "java.lang", TypeTokenEnum.CLASS));

			StandardTypes.interfaces()
					.forEach((interfaceName) -> this.addType(interfaceName, "java.lang", TypeTokenEnum.INTERFACE));

			StandardTypes.enums().forEach((enumName) -> this.addType(enumName, "java.lang", TypeTokenEnum.ENUM));

		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] primitveTypes = { "byte", "short", "int", "long", "float", "double", "char", "boolean" };

		for (String primitve : primitveTypes) {
			this.addType(primitve, "special.primitve", TypeTokenEnum.PRIMITVE);
		}
	}

	public String toString() {
		return typeSpace.toString();
	}
}
