package com.aspodev.Calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import com.aspodev.SCAR.Dependency;
import com.aspodev.SCAR.Method;
import com.aspodev.SCAR.Slice;

public class CalculatorUtil {

    public Set<Method> getParentsMethods(Map<String, Slice> slicesMap, String parentName) {
        try {
            Slice current = slicesMap.get(parentName);
            Set<Method> result = new HashSet<>();
            result.addAll(current.getMethods());
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

    public static Map<String, List<String>> getCohesionMap(Slice slice) {
        String sliceName = slice.getMetaData().getFullName();
        Map<String, List<String>> graph = new HashMap<>();

        // 1. Initialize a list for every method and every attribute
        for (Method m : slice.getMethods()) {
            graph.put(m.getName(), new ArrayList<>());
        }
        for (String attr : slice.getAttributes().stream().map(a -> a.getName()).toList()) {
            graph.put("@" + attr, new ArrayList<>());
        }

        // 2. Helper to add an undirected edge u <-> v
        BiConsumer<String, String> addEdge = (u, v) -> {
            graph.get(u).add(v);
            graph.get(v).add(u);
        };

        // 3. Populate edges
        for (Method m : slice.getMethods()) {
            String caller = m.getName();

            // 3a. Method→Method calls
            m.getDependencies().stream().filter(d -> sliceName.equals(d.getCallerType())).map(Dependency::getName)
                    .distinct().forEach(callee -> addEdge.accept(caller, callee));

            // 3b. Method→Attribute accesses
            m.getAttributeDependencies().stream().distinct().map(a -> "@" + a)
                    .forEach(attrNode -> addEdge.accept(caller, attrNode));
        }

        // 4. Deduplicate each adjacency list
        graph.replaceAll((node, neighbors) -> neighbors.stream().distinct().toList());

        return graph;
    }
}
