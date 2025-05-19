package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
            Set<String> filteredDependencies = new HashSet<>();

            filteredDependencies = dependencies.stream().map(d -> d.getCallerType().replace(".construct", ""))
                    .flatMap(s -> SCAR.getApplicationTypes(s).stream())
                    .filter(s -> !s.equals(i.getValue().getMetaData().name())).collect(Collectors.toSet());

            Double CF = (double) filteredDependencies.size() / (slicesMap.size() - 1);

            result.put(i.getKey(), CF);

        }

        return result;
    }

}