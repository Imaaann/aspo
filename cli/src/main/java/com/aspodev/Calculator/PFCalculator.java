package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Method;
import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;

public class PFCalculator {
    public Map<String, Double> calculate(Model SCAR, Map<String, Double> NOC) {
        {
            Map<String, Double> result = new HashMap<>();
            Map<String, Slice> slicesMap = SCAR.getSliceMap();
            for (Map.Entry<String, Slice> i : slicesMap.entrySet()) {
                String parentName = i.getValue().getParentName();
                if (parentName == null || slicesMap.get(parentName) == null) {
                    result.put(i.getKey(), Double.valueOf(0));
                } else {
                    Set<Method> methods = i.getValue().getMethods();
                    Set<Method> parentMethods = new CalculatorUtil().getAllTreeMethods(slicesMap, parentName);
                    double OverridenMethods = new CalculatorUtil().getOverridenMethodsCount(methods, parentMethods);
                    double newmethods = methods.size() - OverridenMethods;
                    if (newmethods == 0 || NOC.get(i.getKey()).equals(0)) {
                        result.put(i.getKey(), Double.valueOf(1));
                    } else {
                        result.put(i.getKey(), Double.valueOf(OverridenMethods / (newmethods * NOC.get(i.getKey()))));
                    }
                }
            }

            return result;
        }

    }
}
