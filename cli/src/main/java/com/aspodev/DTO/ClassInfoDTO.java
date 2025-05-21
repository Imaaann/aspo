package com.aspodev.DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Method;
import com.aspodev.Calculator.Metrics;
import com.aspodev.SCAR.Dependency;
import com.aspodev.SCAR.Slice;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClassInfoDTO {
    private String name;

    @JsonProperty("package")
    private String _package;

    private String type;
    private double bugProbability;

    @JsonProperty("extends")
    private String extendsClass;

    @JsonProperty("implements")
    private List<String> implementsInterfaces;

    private Map<String, List<DependencyDTO>> dependencies;
    private ClassMetricsDTO metrics;

    public ClassInfoDTO(Slice slice, double bugProbability, Metrics metric) {
        this.name = slice.getMetaData().name();
        this._package = slice.getMetaData().pkg();
        this.type = slice.getMetaData().type().toString();
        this.bugProbability = 0;
        this.extendsClass = slice.getParentName();
        this.implementsInterfaces = slice.getInterfaces().stream().toList();
        this.dependencies = createDependencyDTO(slice);
        this.metrics = new ClassMetricsDTO(metric);

    }

    public Map<String, List<DependencyDTO>> createDependencyDTO(Slice slice) {
        Map<String, List<DependencyDTO>> dependencies = new HashMap<>();
        Set<Method> methods = slice.getMethods();
        for (Method method : methods) {
            List<DependencyDTO> dependencyList = new ArrayList<>();
            Set<Dependency> dependencySet = method.getDependencies();
            String key = method.getName();
            for (Dependency dependency : dependencySet) {

                String dependencyType = dependency.getCallerType().replace(".construct", "");

                if (dependencyType.equals(slice.getMetaData().name())) {
                    String newdependencyName = "this." + dependency.getName();
                    dependencyType = newdependencyName;
                }
                DependencyDTO dependencyDTO = new DependencyDTO(dependencyType);
                if (dependencyList.contains(dependencyDTO)) {
                    dependencyList.get(dependencyList.indexOf(dependencyDTO)).increaseAmount();
                } else {
                    dependencyList.add(dependencyDTO);
                }
            }
            for (String attribute : method.getAttributeDependencies()) {
                DependencyDTO dependencyDTO = new DependencyDTO("@" + attribute);

                dependencyList.add(dependencyDTO);

            }
            dependencies.put(key, dependencyList);

        }
        return dependencies;
        // Getters and SettersS
    }
}
