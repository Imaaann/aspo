package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;
import com.aspodev.utils.GraphTools;

public class LCOM4Calculator implements MetricCalculator {

	@Override
	public Map<String, Double> calculate(Model SCAR) {
		Map<String, Double> result = new HashMap<>();

		for (Map.Entry<String, Slice> entry : SCAR.getSliceMap().entrySet()) {
			Map<String, List<String>> cohesionMap = CalculatorUtil.getCohesionMap(entry.getValue());
			int componentCount = GraphTools.countConnectedComponents(cohesionMap);
			result.put(entry.getKey(), Double.valueOf(componentCount));
		}

		return result;
	}

}
