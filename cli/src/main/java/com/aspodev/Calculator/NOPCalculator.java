package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;
import com.aspodev.SCAR.Method;

public class NOPCalculator implements MetricCalculator {

    @Override
    public Map<String, Double> calculate(Model SCAR) {
        Map<String, Double> result = new HashMap<>();
        Map<String, Slice> slicesMap = SCAR.getSliceMap();

        for (Map.Entry<String, Slice> i : slicesMap.entrySet()) {
            Iterator<Method> method = i.getValue().getMethods().iterator();
            Method temp;
            int NOP = 0;
            while (method.hasNext()) {
                temp = method.next();
                NOP += temp.getArguments().size();
            }
            result.put(i.getKey(), Double.valueOf(NOP / (i.getValue().getMethods().size())));
        }

        return result;
    }

}
