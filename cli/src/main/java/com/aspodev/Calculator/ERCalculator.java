package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.Map;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;

public class ERCalculator implements MetricCalculator {

	@Override
	public Map<String, Double> calculate(Model SCAR) {
		Map<String, Double> result = new HashMap<>();

		for (Map.Entry<String, Slice> entry : SCAR.getSliceMap().entrySet()) {
			Slice slice = entry.getValue();
			long handledExceptionCount = slice.getHandledExceptions().size();
			long thrownExceptionCount = slice.getThrownExceptions().size();
			result.put(entry.getKey(), Double.valueOf(handledExceptionCount - thrownExceptionCount));
		}

		return result;
	}

}
