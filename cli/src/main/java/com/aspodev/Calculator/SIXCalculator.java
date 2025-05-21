package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.Map;

public class SIXCalculator {
    public Map<String, Double> calculate(Map<String, Double> DIT, Map<String, Double> NORM, Map<String, Double> NOM) {
        // Fuck you mouad for using the DIT map here
        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, Double> i : DIT.entrySet()) {
            String currentKey = i.getKey();
            Double DITValue = i.getValue();
            Double NORMValue = NORM.get(currentKey);
            Double NOMValue = NOM.get(currentKey);

            if (NOMValue == 0) {
                result.put(currentKey, 0.0);
            } else {
                result.put(currentKey, (DITValue * NORMValue) / NOMValue);
            }

        }
        return result;
    }

}
