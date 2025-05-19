package com.aspodev.Calculator;

import java.util.Map;

public class SIXCalculator {
    public Map<String, Double> calculate(Map<String, Double> DIT, Map<String, Double> NORM, Map<String, Double> NOM) {
        Map<String, Double> result = DIT;
        for (Map.Entry<String, Double> i : DIT.entrySet()) {
            String currentKey = i.getKey();
            Double DITValue = i.getValue();
            Double NORMValue = NORM.get(currentKey);
            Double NOMValue = NOM.get(currentKey);
            result.put(currentKey, (DITValue * NORMValue) / NOMValue);
        }
        return result;
    }

}
