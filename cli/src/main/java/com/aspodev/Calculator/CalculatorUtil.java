package com.aspodev.Calculator;

import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Method;
import com.aspodev.SCAR.Slice;

public class CalculatorUtil {

    public Set<Method> getParentsMethods(Map<String, Slice> slicesMap, String parentName) {
        try {
            Slice current = slicesMap.get(parentName);
            System.out.println("[DEBUG] current: " + current);
            Set<Method> result = current.getMethods();
            while (!(current.getParentName() == null)) {
                current = slicesMap.get(current.getParentName());
                if (current == null)
                    break;
                System.out.println("[DEBUG] current: " + current);
                result.addAll(current.getMethods());
            }

            return result;
        } catch (NullPointerException e) {
            System.out.println(
                    "[ERROR] NullPointerException: the parentName: " + parentName + " is not in the slicesMap");
            return null;
        }
    }
}
