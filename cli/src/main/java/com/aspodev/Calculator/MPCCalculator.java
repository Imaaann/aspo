package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;
import com.aspodev.SCAR.Dependency;
import com.aspodev.SCAR.Method;

public class MPCCalculator implements MetricCalculator {

    @Override
    public Map<String, Double> calculate(Model SCAR) {
        Map<String, Double> result = new HashMap<>();
        Map<String, Slice> slicesMap = SCAR.getSliceMap();
        int numOfMethodCalls = 0;
        for (Map.Entry<String, Slice> i : slicesMap.entrySet()) {
            Iterator<Method> methodIterator = i.getValue().getMethods().iterator();
            numOfMethodCalls = methodIterator(methodIterator);
            result.put(i.getKey(), Double.valueOf(numOfMethodCalls));
        }

        return result;
    }

    private int getMethodCallsCount(Iterator<Dependency> dependenciesIterator) {
        int numOfMethodCalls = 0;
        while (dependenciesIterator.hasNext()) {
            Set<String> methodCalled = new HashSet<>();
            Dependency currentDependency = dependenciesIterator.next();
            if (!methodCalled.contains(currentDependency.getCallerType())) {
                numOfMethodCalls++;
                methodCalled.add(currentDependency.getCallerType());
            }
        }
        return numOfMethodCalls;
    }

    private int methodIterator(Iterator<Method> methodIterator) {
        int numOfMethodCalls = 0;
        while (methodIterator.hasNext()) {
            Method currentMethod = methodIterator.next();
            Iterator<Dependency> dependenciesIterator = currentMethod.getDependencies().iterator();
            numOfMethodCalls += getMethodCallsCount(dependenciesIterator);
        }
        return numOfMethodCalls;
    }

}
