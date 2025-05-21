package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Method;
import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;

public class NORMCalculator implements MetricCalculator {

    @Override
    public Map<String, Double> calculate(Model SCAR) {
        Map<String, Double> result = new HashMap<>();
        Map<String, Slice> slicesMap = SCAR.getSliceMap();
        Set<Method> parentMethods;
        double numOfOverridenMethods;

        for (Map.Entry<String, Slice> i : slicesMap.entrySet()) {
            Set<Method> methods = i.getValue().getMethods();
            String parentName = i.getValue().getParentName();
            if (parentName == null || slicesMap.get(parentName) == null) {
                System.out.println();
                result.put(i.getKey(), Double.valueOf(0));
            } else {
                parentMethods = new CalculatorUtil().getParentsMethods(slicesMap, parentName);
                numOfOverridenMethods = new CalculatorUtil().getOverridenMethodsCount(methods, parentMethods);

                result.put(i.getKey(), Double.valueOf(numOfOverridenMethods / methods.size()));
            }

        }

        return result;
    }

}
