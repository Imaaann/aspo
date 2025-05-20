package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;
import com.aspodev.utils.GraphTools;

public class LCCCalculator implements MetricCalculator {

	@Override
	public Map<String, Double> calculate(Model SCAR) {
		Map<String, Double> result = new HashMap<>();

		for (Map.Entry<String, Slice> entry : SCAR.getSliceMap().entrySet()) {
			Map<String, List<String>> cohesionMap = CalculatorUtil.getCohesionMap(entry.getValue());
			Map<String, Set<String>> reachMap = GraphTools.computeReachabilityMap(cohesionMap);

			long reachablePairs = reachMap.values().stream().mapToLong(Set::size).sum();
			int methodCount = cohesionMap.size();
			Double LCC = (methodCount < 2) ? 1.0 : (double) reachablePairs / (methodCount * (methodCount - 1));

			result.put(entry.getKey(), LCC);

		}

		return result;
	}

}
