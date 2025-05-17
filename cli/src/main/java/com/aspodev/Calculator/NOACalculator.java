package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.Map;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;

public class NOACalculator implements MetricCalculator {
    @Override
    public Map<String, Double> calculate(Model SCAR) {
        Map<String, Double> result = new HashMap<>();
        Map<String, Slice> slicesMap = SCAR.getSliceMap();

        for (Map.Entry<String, Slice> i : slicesMap.entrySet()) {
            result.put(i.getKey(), Double.valueOf(i.getValue().getAttributes().size()));
        }
        return result;
    }
}