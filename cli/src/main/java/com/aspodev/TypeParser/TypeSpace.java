package com.aspodev.TypeParser;

import java.util.ArrayList;
import java.util.List;

public class TypeSpace {
	private ArrayList<TypeToken> typeSpace;

	public TypeSpace() {
		this.typeSpace = new ArrayList<>();
		this.loadStandardTypes();
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

	private void loadStandardTypes() {

	}
}
