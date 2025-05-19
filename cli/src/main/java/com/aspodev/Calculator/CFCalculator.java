package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Dependency;
import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;

public class CFCalculator implements MetricCalculator {
    @Override
    public Map<String, Double> calculate(Model SCAR) {
        Map<String, Double> result = new HashMap<>();
        Map<String, Slice> slicesMap = SCAR.getSliceMap();
        for (Map.Entry<String, Slice> i : slicesMap.entrySet()) {
            Set<Dependency> dependencies = i.getValue().getDependencies();
            Set<String> filteredDependecies = new HashSet<>();
            ;
            if (dependencies.size() > 0) {
                for (Dependency d : dependencies) {
                    String callerType = d.getCallerType();
                    if (!isDependencyValid(i.getValue().getMetaData().name(), callerType, slicesMap)) {
                        filteredDependecies.add(callerType);
                    }
                }
                result.put(i.getKey(), Double.valueOf(filteredDependecies.size() / slicesMap.size()));
            }

        }
        return result;
    }

    private boolean isDependencyValid(String key, String callerType, Map<String, Slice> slicesMap) {
        return slicesMap.containsKey(callerType) && callerType.contains(".construct") && callerType.contains(key);
    }

}