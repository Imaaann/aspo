package com.aspodev.Calculator;

import java.util.Map;
import java.util.Map.Entry;

public class STLOCCalculator {

    public Double calculateSTLOC(Map<String, Metrics> metrics) {
        Double STLOC = 0.0;
        for (Entry<String, Metrics> entry : metrics.entrySet()) {
            STLOC += entry.getValue().getMetricValue("TLOC");
        }
        return Double.valueOf(STLOC);
    }
}
