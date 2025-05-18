package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.Map;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;

public class TLOCCalculation implements MetricCalculator {

	@Override
	public Map<String, Double> calculate(Model SCAR) {
		Map<String, Double> result = new HashMap<>();

		for (Map.Entry<String, Slice> entry : SCAR.getSliceMap().entrySet()) {
			result.put(entry.getKey(), Double.valueOf(entry.getValue().getInstructionNumber()));
		}

		return result;
	}

}
