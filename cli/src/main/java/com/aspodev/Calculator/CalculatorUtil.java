package com.aspodev.Calculator;

import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Method;
import com.aspodev.SCAR.Slice;

public class CalculatorUtil {
    public Set<Method> getParentsMethods(Map<String, Slice> slicesMap, String parentName) {
        Slice current = slicesMap.get(parentName);
        Set<Method> result = current.getMethods();
        while (!(current.getParentName() == null)) {
            current = slicesMap.get(current.getParentName());
            if (current == null)
                break;

        }
        result.addAll(current.getMethods());

        return result;
    }
}
