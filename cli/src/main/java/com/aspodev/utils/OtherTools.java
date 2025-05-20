package com.aspodev.utils;

import java.util.Map;
import java.util.stream.Collectors;

import com.aspodev.Calculator.Metrics;

public class OtherTools {
	public static String resultMapToString(Map<String, Metrics> map) {
		return map.entrySet().stream().map(e -> e.getKey() + " -> " + e.getValue())
				.collect(Collectors.joining(System.lineSeparator()));
	}
}
