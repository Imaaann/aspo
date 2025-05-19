package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;

public class NOCCalculator implements MetricCalculator {
    @Override
    public Map<String, Double> calculate(Model SCAR) {
        Map<String, Double> result = new HashMap<>();
        Map<String, Slice> slicesMap = SCAR.getSliceMap();

        for (Map.Entry<String, Slice> i : slicesMap.entrySet()) {
            Set<String> childSet = new HashSet<>();
            childSet.add(i.getKey());
            boolean hasChildren = true;
            while (hasChildren) {
                hasChildren = false;
                for (Map.Entry<String, Slice> j : slicesMap.entrySet()) {
                    String parentName = j.getValue().getParentName();
                    if (parentName != null && childSet.contains(parentName) && !childSet.contains(j.getKey())) {
                        childSet.add(j.getKey());
                        hasChildren = true;
                    }
                }
            }
            System.out.println("Debug: NOC for " + i.getKey() + " is " + (childSet.size() - 1));
            result.put(i.getKey(), Double.valueOf(childSet.size() - 1));
        }
        return result;
    }
}
