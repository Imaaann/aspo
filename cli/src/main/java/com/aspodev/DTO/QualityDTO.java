package com.aspodev.DTO;

import java.util.Map;

import com.aspodev.Calculator.Metrics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QualityDTO {
    private RiskDTO lowRisk;
    private RiskDTO mediumRisk;
    private RiskDTO highRisk;

    private final Double LOW_RISK_THRESHOLD = 0.3;
    private final Double MEDIUM_RISK_THRESHOLD = 0.6;

    public QualityDTO(Map<String, Metrics> metrics) {
        RiskDTO lowRisk = new RiskDTO();
        RiskDTO mediumRisk = new RiskDTO();
        RiskDTO highRisk = new RiskDTO();
        Double totalClasses = Double.valueOf(metrics.size());
        for (Map.Entry<String, Metrics> entry : metrics.entrySet()) {
            Double BugP = entry.getValue().getMetricValue("BUGP");
            if (BugP != null) {
                if (BugP <= LOW_RISK_THRESHOLD) {
                    lowRisk.increaseNOC();

                } else if (BugP <= MEDIUM_RISK_THRESHOLD) {
                    mediumRisk.increaseNOC();

                } else {
                    highRisk.increaseNOC();

                }
            }

        }

        lowRisk.setValue((lowRisk.getNOC() / totalClasses) * 100);
        mediumRisk.setValue((mediumRisk.getNOC() / totalClasses) * 100);
        highRisk.setValue((highRisk.getNOC() / totalClasses) * 100);

        this.lowRisk = lowRisk;
        this.mediumRisk = mediumRisk;
        this.highRisk = highRisk;
    }

    public RiskDTO getLowRisk() {
        return lowRisk;
    }

    public RiskDTO getMediumRisk() {
        return mediumRisk;
    }

    public RiskDTO getHighRisk() {
        return highRisk;
    }
}
