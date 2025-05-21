package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.aspodev.SCAR.Dependency;
import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;

public class CBOCalculator implements MetricCalculator {

	@Override
	public Map<String, Double> calculate(Model SCAR) {
		Map<String, Double> result = new HashMap<>();
		Map<String, Slice> slices = SCAR.getSliceMap();

		for (Map.Entry<String, Slice> entry : slices.entrySet()) {
			Set<Dependency> dependencies = entry.getValue().getDependencies();

			Set<String> filteredSet = dependencies.stream().map(d -> {
				String str = d.getCallerType();
				str = str.replace(".construct", "");
				return str;
			}).filter(s -> !s.equals(entry.getValue().getMetaData().name())).collect(Collectors.toSet());

			Double CBO = Double.valueOf(filteredSet.size());
			result.put(entry.getKey(), CBO);
		}

		return result;
	}

}
