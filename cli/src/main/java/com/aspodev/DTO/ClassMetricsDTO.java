package com.aspodev.DTO;

import com.aspodev.Calculator.Metrics;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassMetricsDTO {
    private double CBO;
    private double RFC;
    private double LCOM4;
    private double ED;
    private double NOC;
    private double MPC;
    private double NOA;
    private double NOM;
    private double NOP;
    private double PF;
    private double TLOC;
    private double NSM;
    private double MIT;
    private double DAC;
    private double DIT;
    private double DAM;
    private double SIX;
    private double CF;
    private double LCC;
    private double NOLM;
    private double NORM;

    public ClassMetricsDTO(Metrics metrics) {
        CBO = metrics.getMetricValue("CBO");
        RFC = metrics.getMetricValue("RFC");
        LCOM4 = metrics.getMetricValue("LCOM4");
        ED = metrics.getMetricValue("ED");
        NOC = metrics.getMetricValue("NOC");
        MPC = metrics.getMetricValue("MPC");
        NOA = metrics.getMetricValue("NOA");
        NOM = metrics.getMetricValue("NOM");
        NOP = metrics.getMetricValue("NOP");
        PF = metrics.getMetricValue("PF");
        TLOC = metrics.getMetricValue("TLOC");
        NSM = metrics.getMetricValue("NSM");
        MIT = metrics.getMetricValue("MIT");
        DAC = metrics.getMetricValue("DAC");
        DIT = metrics.getMetricValue("DIT");
        DAM = metrics.getMetricValue("DAM");
        SIX = metrics.getMetricValue("SIX");
        CF = metrics.getMetricValue("CF");
        LCC = metrics.getMetricValue("LCC");
        NOLM = metrics.getMetricValue("NOLM");
        NORM = metrics.getMetricValue("NORM");
    }

    @JsonProperty("CBO")
    public double getCBO() {
        return CBO;
    }

    @JsonProperty("RFC")
    public double getRFC() {
        return RFC;
    }

    @JsonProperty("LCOM4")
    public double getLCOM() {
        return LCOM4;
    }

    @JsonProperty("ED")
    public double getED() {
        return ED;
    }

    @JsonProperty("NOC")
    public double getNOC() {
        return NOC;
    }

    @JsonProperty("MPC")
    public double getMPC() {
        return MPC;
    }

    @JsonProperty("NOA")
    public double getNOA() {
        return NOA;
    }

    @JsonProperty("NOM")
    public double getNOM() {
        return NOM;
    }

    @JsonProperty("NOP")
    public double getNOP() {
        return NOP;
    }

    @JsonProperty("PF")
    public double getPF() {
        return PF;
    }

    @JsonProperty("TLOC")
    public double getTLOC() {
        return TLOC;
    }

    @JsonProperty("RSM")
    public double getNSM() {
        return NSM;
    }

    @JsonProperty("MIT")
    public double getMIT() {
        return MIT;
    }

    @JsonProperty("DAC")
    public double getDAC() {
        return DAC;
    }

    @JsonProperty("DIT")
    public double getDIT() {
        return DIT;
    }

    @JsonProperty("DAM")
    public double getDAM() {
        return DAM;
    }

    @JsonProperty("SIX")
    public double getSIX() {
        return SIX;
    }

    @JsonProperty("CF")
    public double getCF() {
        return CF;
    }

    @JsonProperty("LCC")
    public double getLCC() {
        return LCC;
    }

    @JsonProperty("NOLM")
    public double getNOLM() {
        return NOLM;
    }

    @JsonProperty("NORM")
    public double getNORM() {
        return NORM;
    }
}
