package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Method;
import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;

public class NOLMCalculator implements MetricCalculator {

    @Override
    public Map<String, Double> calculate(Model SCAR) {
        Map<String, Double> result = new HashMap<>();
        Map<String, Slice> slicesMap = SCAR.getSliceMap();
        int numOfOverLoadedMethods = 0;
        for (Map.Entry<String, Slice> i : slicesMap.entrySet()) {
            numOfOverLoadedMethods = getOverloadedMethodsCount(i.getValue());

            System.out.println("[DEBUG] NOLM result: " + numOfOverLoadedMethods + " For class: " + i.getKey());
            result.put(i.getKey(), Double.valueOf(numOfOverLoadedMethods));
        }
        return result;
    }

    private int getOverloadedMethodsCount(Slice slice) {
        int numOfOverLoadedMethods = 0;
        Set<String> checked = new HashSet<>();
        Iterator<Method> methods = slice.getMethods().iterator();
        while (methods.hasNext()) {
            Method method = methods.next();
            if (!checked.contains(method.getName())) {
                checked.add(method.getName());
                Iterator<Method> OLcheck = slice.getMethods().iterator();
                while (OLcheck.hasNext()) {
                    Method OLmethod = OLcheck.next();
                    if (method.getName().equals(OLmethod.getName()) && method != OLmethod) {
                        numOfOverLoadedMethods++;

                    }
                }
            }
        }
        return numOfOverLoadedMethods;
    }

}
