package com.aspodev.utils;

import java.util.Map;
import java.util.stream.Collectors;

public class OtherTools {
	public static <K, V> String resultMapToString(Map<K, V> map) {
		return map.entrySet().stream().map(e -> e.getKey() + " -> " + e.getValue())
				.collect(Collectors.joining(System.lineSeparator()));
	}
}
