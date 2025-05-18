package com.aspodev.Calculator;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.InheritanceRelation;
import com.aspodev.SCAR.RelationTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DITCalculator implements MetricCalculator {

    @Override
    public Map<String, Double> calculate(Model SCAR) {
        Map<String, Double> result = new HashMap<>();
        Map<String, List<InheritanceRelation>> inheritanceGraph = SCAR.getInheritanceGraph();

        for (String sliceName : inheritanceGraph.keySet()) {
            int depth = calculateDepth(sliceName, inheritanceGraph);
            result.put(sliceName,Double.valueOf(depth));
        }


        return result;
    }

    private int calculateDepth(String sliceName, Map<String, List<InheritanceRelation>> inheritanceGraph) {
        int depth = 0;
        String current = sliceName;

        while (inheritanceGraph.containsKey(current)) {
            List<InheritanceRelation> relations = inheritanceGraph.get(current);
            boolean hasParent = false;

            for (InheritanceRelation relation : relations) {
                if (relation.type() == RelationTypes.EXTENDS) {
                    current = relation.name();
                    hasParent = true;
                    depth++;
                    break;
                }
            }

            if (!hasParent) {
                break;
            }
        }

        return depth;
    }
}