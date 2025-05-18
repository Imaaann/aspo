package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;
import com.aspodev.SCAR.Method;;

public class MITCalculator implements MetricCalculator {

    @Override
    public Map<String, Double> calculate(Model SCAR) {
        Map<String, Double> result = new HashMap<>();
        Map<String, Slice> slicesMap = SCAR.getSliceMap();
        String parentName;
        Set<Method> parentMethods;

        for (Map.Entry<String, Slice> i : slicesMap.entrySet()) {
            parentName = i.getValue().getParentName();
            if (parentName == null || slicesMap.get(parentName) == null) {
                System.out.println("[DEBUG] MIT result: 0 For class: " + i.getKey() + " " + parentName + " "
                        + slicesMap.get(parentName));
                result.put(i.getKey(), Double.valueOf(0));
            } else {
                parentMethods = new CalculatorUtil().getParentsMethods(slicesMap, parentName);
                System.out.println("parents exists" + parentMethods);
                System.out.println("[DEBUG] MIT result: " + parentMethods.size() + " For class: " + i.getKey());
                result.put(i.getKey(), Double.valueOf(parentMethods.size()));
            }
        }

        return result;
    }

}
