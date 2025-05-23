package com.aspodev.DTO;

import java.util.HashMap;
import java.util.Map;

import com.aspodev.Calculator.AHFCalculator;
import com.aspodev.Calculator.MHFCalculator;
import com.aspodev.Calculator.Metrics;
import com.aspodev.Calculator.NOICalculator;
import com.aspodev.Calculator.SNOCCalculator;
import com.aspodev.Calculator.STLOCCalculator;
import com.aspodev.Calculator.SystemMetrics;
import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;

public class SystemResultDTO {
    private String projectName;
    private SystemMetricsDTO systemMetrics;
    private Map<String, InheritanceInfoDTO> inheritance;
    private Map<String, ClassInfoDTO> classes;

    public SystemResultDTO(String projectName, Model SCAR, Map<String, Double> TLOC, QualityDTO quality,
            Metrics metrics) {
        this.projectName = projectName;
        this.systemMetrics = systemMetrics(SCAR, TLOC, quality);
        this.inheritance = inheritance(SCAR);
        this.classes = classes(SCAR, metrics);
    }

    private SystemMetricsDTO systemMetrics(Model SCAR, Map<String, Double> TLOC, QualityDTO quality) {
        SystemMetrics systemMetrics = new SystemMetrics();
        systemMetrics.insertMetric("NOC", new SNOCCalculator().calculate(SCAR));
        systemMetrics.insertMetric("TLOC", new STLOCCalculator().calculateSTLOC(TLOC));
        systemMetrics.insertMetric("NOI", new NOICalculator().calculate(SCAR));
        systemMetrics.insertMetric("MHF", new MHFCalculator().calculate(SCAR));
        systemMetrics.insertMetric("AHF", new AHFCalculator().calculate(SCAR));

        return new SystemMetricsDTO(systemMetrics, quality);

    }

    private Map<String, InheritanceInfoDTO> inheritance(Model SCAR) {
        Map<String, Slice> slicesMap = SCAR.getSliceMap();
        Map<String, InheritanceInfoDTO> inheritance = new HashMap<>();
        for (Map.Entry<String, Slice> entry : slicesMap.entrySet()) {
            Slice slice = entry.getValue();
            InheritanceInfoDTO inheritanceInfo = new InheritanceInfoDTO(slice);
            inheritance.put(entry.getKey(), inheritanceInfo);
        }
        return inheritance;
    }

    private Map<String, ClassInfoDTO> classes(Model SCAR, Metrics metrics) {
        Map<String, Slice> slicesMap = SCAR.getSliceMap();
        Map<String, ClassInfoDTO> classes = new HashMap<>();
        for (Map.Entry<String, Slice> entry : slicesMap.entrySet()) {
            Slice slice = entry.getValue();
            ClassInfoDTO classInfo = new ClassInfoDTO(slice, 0, metrics);
            classes.put(entry.getKey(), classInfo);
        }
        return classes;
    }

}
