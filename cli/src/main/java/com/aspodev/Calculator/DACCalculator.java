package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Attribute;
import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;

public class DACCalculator implements MetricCalculator {

	@Override
	public Map<String, Double> calculate(Model SCAR) {
		Map<String, Double> result = new HashMap<>();

		for (Map.Entry<String, Slice> entry : SCAR.getSliceMap().entrySet()) {
			Set<Attribute> attributes = entry.getValue().getAttributes();
			long DAC = attributes.stream().filter(a -> SCAR.isApplicationType(a.getType())).map(a -> a.getType())
					.distinct().count();
			result.put(entry.getKey(), Double.valueOf(DAC));
		}

		return result;
	}

}
