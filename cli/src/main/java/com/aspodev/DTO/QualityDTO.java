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

    public QualityDTO(Map<String, Metrics> metrics) {
        RiskDTO lowRisk = new RiskDTO();
        RiskDTO mediumRisk = new RiskDTO();
        RiskDTO highRisk = new RiskDTO();
        Double totalClasses = Double.valueOf(metrics.size());
        for (Map.Entry<String, Metrics> entry : metrics.entrySet()) {
            String key = entry.getKey();
            Double BugP = entry.getValue().getMetricValue("BUGP");
            if (BugP != null) {
                if (BugP < 0.1) {
                    lowRisk.increaseNOC();

                } else if (BugP < 0.3) {
                    mediumRisk.increaseNOC();

                } else {
                    highRisk.increaseNOC();

                }
            }

        }

        lowRisk.setValue(lowRisk.getNOC() / totalClasses);
        mediumRisk.setValue(mediumRisk.getNOC() / totalClasses);
        highRisk.setValue(highRisk.getNOC() / totalClasses);

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
