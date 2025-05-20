package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Dependency;
import com.aspodev.SCAR.Method;
import com.aspodev.SCAR.Slice;

public class CalculatorUtil {

    public Set<Method> getParentsMethods(Map<String, Slice> slicesMap, String parentName) {
        try {
            Slice current = slicesMap.get(parentName);
            Set<Method> result = new HashSet<>();
            while (!(current.getParentName() == null)) {
                current = slicesMap.get(current.getParentName());
                if (current == null)
                    break;
                result.addAll(current.getMethods());
            }

            return result;
        } catch (NullPointerException e) {
            System.out.println(
                    "[ERROR] NullPointerException: the parentName: " + parentName + " is not in the slicesMap");
            return null;
        }
    }

    public Set<Method> getAllTreeMethods(Map<String, Slice> slicesMap, String parentName) {
        try {
            Slice current = slicesMap.get(parentName);
            Set<Method> result = current.getMethods();
            while (!(current.getParentName() == null)) {
                current = slicesMap.get(current.getParentName());
                if (current == null)
                    break;
                result.addAll(current.getMethods());
            }

            return result;
        } catch (NullPointerException e) {
            System.out.println(
                    "[ERROR] NullPointerException: the parentName: " + parentName + " is not in the slicesMap");
            return null;
        }
    }

    public int getOverridenMethodsCount(Set<Method> methods, Set<Method> parentMethods) {
        int numOfOverridenMethods = 0;
        Set<String> checked = new HashSet<>();
        for (Method method : methods) {
            if (checked.contains(method.getName())) {
                continue;
            }
            Iterator<Method> parentMethodsIterator = parentMethods.iterator();
            while (parentMethodsIterator.hasNext()) {
                Method parentMethod = parentMethodsIterator.next();
                if (method.getName().equals(parentMethod.getName())) {
                    numOfOverridenMethods++;
                    break;
                }
            }
            checked.add(method.getName());
        }
        return numOfOverridenMethods;
    }

    public Map<String, List<String>> getCohesionMap(Slice slice) {
        String sliceName = slice.getMetaData().getFullName();
        Map<String, List<String>> cohesionMap = new HashMap<>();

        for (Method method : slice.getMethods()) {
            String methodName = method.getName();
            List<String> selfDependencies = method.getDependencies().stream()
                    .filter(d -> d.getCallerType().equals(sliceName)).map(Dependency::getName).distinct().toList();

            cohesionMap.put(methodName, selfDependencies);
        }

        return cohesionMap;
    }

}
