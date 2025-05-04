package com.aspodev.parser;

import java.util.HashMap;
import java.util.Map;

public class LocalVariableMap {
	private Map<Integer, Map<String, String>> map;

	public LocalVariableMap() {
		this.map = new HashMap<>();
	}

	public void addVariable(int scope, String typeName, String varName) {
		Map<String, String> scopeMap = map.get(scope);

		if (scopeMap == null) {
			scopeMap = new HashMap<>();
			map.put(scope, scopeMap);
		}

		scopeMap.put(typeName, varName);
	}

	public String getVariableType(int scope, String varName) {
		return map.get(scope).get(varName);
	}

	public void removeScope(int scope) {
		map.remove(scope);
	}

}
