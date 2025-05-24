package com.aspodev.Calculator;

import com.aspodev.SCAR.Model;

public class SNOCCalculator {
    public Double calculate(Model SCAR) {
        return Double.valueOf(SCAR.getSliceMap().size());
    }

}
