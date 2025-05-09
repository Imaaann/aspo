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

		System.out.println("[DEBUG] == adding variable: " + varName + " : " + typeName + " to scope number " + scope);

		if (scopeMap == null) {
			scopeMap = new HashMap<>();
			map.put(scope, scopeMap);
		}

		scopeMap.put(varName, typeName);
	}

	public String getVariableType(int scope, String varName) {

		for (int i = 1; i <= scope; i++) {
			Map<String, String> localMap = map.get(i);

			if (localMap == null)
				continue;

			String typeName = localMap.get(varName);

			if (typeName != null)
				return typeName;
		}

		return null;
	}

	public void removeScope(int scope) {
		map.remove(scope);
	}

}
