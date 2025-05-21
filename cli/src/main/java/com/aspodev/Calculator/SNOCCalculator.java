package com.aspodev.Calculator;

import com.aspodev.SCAR.Model;

public class SNOCCalculator {
    public double calculate(Model SCAR) {
        return SCAR.getSliceMap().size();
    }

}
