package com.aspodev.DTO;

import com.aspodev.Calculator.SystemMetrics;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public double getNOC() {
        return NOC;
    }

    public double getNOI() {
        return NOI;
    }

    public double getMHF() {
        return MHF;
    }

    public double getAHF() {
        return AHF;
    }

    public double getTLOC() {
        return TLOC;
    }

    public QualityDTO getQuality() {
        return quality;
    }

}
