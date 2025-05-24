package com.aspodev.Calculator;

import java.util.Map;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;
import com.aspodev.TypeParser.TypeTokenEnum;

public class NOICalculator {
    public Double calculate(Model SCAR) {
        Double NOI = 0.0;
        Map<String, Slice> slicesMap = SCAR.getSliceMap();
        for (Map.Entry<String, Slice> entry : slicesMap.entrySet()) {
            if (entry.getValue().getMetaData().type() == TypeTokenEnum.INTERFACE)
                NOI++;
        }
        return NOI;
    }
}
