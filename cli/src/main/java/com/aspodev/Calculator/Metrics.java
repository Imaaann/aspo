package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Metrics {
	private final Map<String, Double> values;

	public Metrics() {
		values = new HashMap<>(21);
	}

	public void insertMetric(String metricName, Double metricValue) {
		values.put(metricName, metricValue);
	}

	public List<Double> getMetricValues() {
		return values.values().stream().toList();
	}

	public Double getMetricValue(String metricName) {
		return values.get(metricName);
	}

	@Override
	public String toString() {
		// produces:
		// {
		// "accuracy": 0.1,
		// "precision": 0.87
		// }
		return values.entrySet().stream().map(e -> "  \"" + e.getKey() + "\": " + e.getValue())
				.collect(Collectors.joining(",\n", "{\n", "\n}"));
	}
}
