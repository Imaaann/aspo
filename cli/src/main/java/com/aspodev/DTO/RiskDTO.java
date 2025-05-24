package com.aspodev.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiskDTO {
    private double value;
    private int NOC;

    public int getNOC() {
        return NOC;
    }

    public double getValue() {
        return value;
    }

    public void increaseNOC() {
        this.NOC++;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
