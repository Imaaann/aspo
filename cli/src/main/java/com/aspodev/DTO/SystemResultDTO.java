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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemResultDTO {
    private String projectName;
    private SystemMetricsDTO systemMetrics;
    private Map<String, InheritanceInfoDTO> inheritance;
    private Map<String, ClassInfoDTO> classes;

    public SystemResultDTO(String projectName, Model SCAR, Map<String, Metrics> metrics) {
        this.projectName = projectName;
        this.systemMetrics = systemMetrics(SCAR, metrics, new QualityDTO(metrics));
        this.inheritance = inheritance(SCAR);
        this.classes = classes(SCAR, metrics);
    }

    public String getProjectName() {
        return projectName;
    }

    public SystemMetricsDTO getSystemMetrics() {
        return systemMetrics;
    }

    public Map<String, InheritanceInfoDTO> getInheritance() {
        return inheritance;
    }

    public Map<String, ClassInfoDTO> getClasses() {
        return classes;
    }

    private SystemMetricsDTO systemMetrics(Model SCAR, Map<String, Metrics> metrics, QualityDTO quality) {
        SystemMetrics systemMetrics = new SystemMetrics();
        systemMetrics.insertMetric("NOC", new SNOCCalculator().calculate(SCAR));
        systemMetrics.insertMetric("TLOC", new STLOCCalculator().calculateSTLOC(metrics));
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

    private Map<String, ClassInfoDTO> classes(Model SCAR, Map<String, Metrics> metrics) {
        Map<String, Slice> slicesMap = SCAR.getSliceMap();
        Map<String, ClassInfoDTO> classes = new HashMap<>();
        for (Map.Entry<String, Slice> entry : slicesMap.entrySet()) {
            Slice slice = entry.getValue();
            ClassInfoDTO classInfo = new ClassInfoDTO(slice, metrics.get(entry.getKey()));
            classes.put(entry.getKey(), classInfo);
        }
        return classes;
    }

}
