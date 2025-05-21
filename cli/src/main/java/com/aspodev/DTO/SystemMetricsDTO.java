package com.aspodev.DTO;

import com.aspodev.Calculator.SystemMetrics;

public class SystemMetricsDTO {
    private double NOC;
    private double NOI;
    private double MHF;
    private double AHF;
    private double TLOC;
    private QualityDTO quality;

    public SystemMetricsDTO(SystemMetrics systemMetrics, QualityDTO quality) {
        this.NOC = systemMetrics.getMetricValue("NOC");
        this.NOI = systemMetrics.getMetricValue("NOI");
        this.NOI = systemMetrics.getMetricValue("NOI");
        this.MHF = systemMetrics.getMetricValue("MHF");
        this.AHF = systemMetrics.getMetricValue("AHF");
        this.TLOC = systemMetrics.getMetricValue("TLOC");
        this.quality = quality;
    }
}
