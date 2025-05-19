package com.aspodev.SCAR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.aspodev.parser.Instructions.InstructionUtil;

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

	public Map<String, Slice> getSliceMap() {
		return slicesMap;
	}

	public void createInheritanceGraph() {

		for (Slice slice : slicesMap.values()) {
			String childFullName = slice.getMetaData().getFullName();

			List<InheritanceRelation> relations = inheritanceGraph.computeIfAbsent(childFullName,
					key -> new ArrayList<>());

			if (slice.getParentName() != null) {
				relations.add(new InheritanceRelation(slice.getParentName(), RelationTypes.EXTENDS));
			}

			for (String interfaceName : slice.getInterfaces()) {
				relations.add(new InheritanceRelation(interfaceName, RelationTypes.IMPLEMENTS));
			}
		}
	}

	public Map<String, List<InheritanceRelation>> getInheritanceGraph() {
		return inheritanceGraph;
	}

	public boolean isApplicationType(String typeName) {
		// Case1: full type name like com.aspodev.SCAR.Slice
		if (slicesMap.containsKey(typeName))
			return true;

		Set<String> typeNameSet = InstructionUtil.extractNames(typeName);

		Set<String> applicationTypes = slicesMap.keySet().stream().map(type -> {
			String last = Arrays.stream(type.split("\\.")).reduce((a, b) -> b).orElse("");
			return last;
		}).collect(Collectors.toSet());

		for (String name : typeNameSet) {
			if (applicationTypes.contains(name))
				return true;
		}

		return false;
	}

	public Set<String> getApplicationTypes(String typeName) {
		Set<String> result = new HashSet<>();
		Set<String> typeNameSet = InstructionUtil.extractNames(typeName);
		Set<String> applicationTypes = slicesMap.keySet().stream().map(type -> {
			String last = Arrays.stream(type.split("\\.")).reduce((a, b) -> b).orElse("");
			return last;
		}).collect(Collectors.toSet());

		for (String type : typeNameSet) {
			if (applicationTypes.contains(type)) {
				result.add(type);
			}
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		// 1) List all slices
		sb.append("Model Slices (").append(slicesMap.size()).append("):\n");
		for (Slice sl : slicesMap.values()) {
			sb.append("  - ").append(sl.getMetaData().getFullName()).append(": \n")
					.append(sl.toString().replaceAll("(?m)^", "    ")) // indent slice.toString()
					.append("\n\n");
		}

		// 2) Print the inheritance graph
		sb.append("Inheritance Graph (").append(inheritanceGraph.size()).append(" nodes):\n");
		// sort keys for stable output
		inheritanceGraph.keySet().stream().sorted().forEach(parent -> {
			sb.append("  ").append(parent).append("\n");
			List<InheritanceRelation> relations = inheritanceGraph.get(parent);
			// group by relation type for nicer grouping, or just list
			relations.forEach(rel -> {
				sb.append("    ").append(rel.type().name().toLowerCase()).append(" → ").append(rel.name()).append("\n");
			});
			sb.append("\n");
		});

		return sb.toString();
	}

}
