package com.aspodev.SCAR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
	private final Map<String, Slice> slicesMap;
	private final Map<String, List<InheritanceRelation>> inheritanceGraph;

	public Model() {
		this.slicesMap = new HashMap<>();
		this.inheritanceGraph = new HashMap<>();
	}

	public void addSlice(Slice slice) {
		slicesMap.put(slice.getMetaData().getFullName(), slice);
	}

	/**
	 * since the model stores the slices as their full name "ex:
	 * com.aspodev.SCAR.Model" you need to provide both arguments
	 * 
	 * @param name    name of the class being queried
	 * @param pkgName package of the class being queries
	 *
	 * @return the Slice object of the queried class
	 */
	public Slice getSlice(String name, String pkgName) {
		return slicesMap.get(pkgName + "." + name);
	}

	public void createInheritanceGraph() {
		// TODO: make this the scar finalizer
	}
}
