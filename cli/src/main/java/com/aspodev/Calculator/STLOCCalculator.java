package com.aspodev.Calculator;

import java.util.Map;

public class STLOCCalculator {

    public Double calculateSTLOC(Map<String, Double> TLOC) {
        Double STLOC = 0.0;
        for (Map.Entry<String, Double> entry : TLOC.entrySet()) {
            STLOC += entry.getValue();
        }
        return Double.valueOf(STLOC);
    }
}
