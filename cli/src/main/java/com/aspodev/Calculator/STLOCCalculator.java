package com.aspodev.Calculator;

import java.util.Map;

public class STLOCCalculator {

    public double calculateSTLOC(Map<String, Double> TLOC) {
        double STLOC = 0.0;
        for (Map.Entry<String, Double> entry : TLOC.entrySet()) {
            STLOC += entry.getValue();
        }
        return STLOC;
    }
}
