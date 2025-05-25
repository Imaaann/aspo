package com.aspodev.DTO;

import com.aspodev.Calculator.SystemMetrics;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("NOC")
    public double getNOC() {
        return NOC;
    }

    @JsonProperty("NOI")
    public double getNOI() {
        return NOI;
    }

    @JsonProperty("MHF")
    public double getMHF() {
        return MHF;
    }

    @JsonProperty("AHF")
    public double getAHF() {
        return AHF;
    }

    @JsonProperty("TLOC")
    public double getTLOC() {
        return TLOC;
    }

    public QualityDTO getQuality() {
        return quality;
    }

}
