package com.aspodev.DTO;

public class QualityDTO {
    private RiskDTO lowRisk;
    private RiskDTO mediumRisk;
    private RiskDTO highRisk;

    public QualityDTO(RiskDTO lowRisk, RiskDTO mediumRisk, RiskDTO highRisk) {
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
