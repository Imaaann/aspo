package com.aspodev.DTO;

import com.aspodev.Calculator.Metrics;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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

    public double getCBO() {
        return CBO;
    }

    public double getRFC() {
        return RFC;
    }

    public double getLCOM() {
        return LCOM4;
    }

    public double getED() {
        return ED;
    }

    public double getNOC() {
        return NOC;
    }

    public double getMPC() {
        return MPC;
    }

    public double getNOA() {
        return NOA;
    }

    public double getNOM() {
        return NOM;
    }

    public double getNOP() {
        return NOP;
    }

    public double getPF() {
        return PF;
    }

    public double getTLOC() {
        return TLOC;
    }

    public double getNSM() {
        return NSM;
    }

    public double getMIT() {
        return MIT;
    }

    public double getDAC() {
        return DAC;
    }

    public double getDIT() {
        return DIT;
    }

    public double getDAM() {
        return DAM;
    }

    public double getSIX() {
        return SIX;
    }

    public double getCF() {
        return CF;
    }

    public double getLCC() {
        return LCC;
    }

    public double getNOLM() {
        return NOLM;
    }

    public double getNORM() {
        return NORM;
    }
}
