package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Accessors;
import com.aspodev.SCAR.Attribute;
import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;

public class DAMCalculator implements MetricCalculator {

    @Override
    public Map<String, Double> calculate(Model SCAR) {
        Map<String, Double> result = new HashMap<>();
        Map<String, Slice> slicesMap = SCAR.getSliceMap();
        Set<Attribute> attributes;
        Iterator<Attribute> attributeIterator;
        Accessors currentAccessor;
        int NumOfEncapsulation = 0;

        for (Map.Entry<String, Slice> i : slicesMap.entrySet()) {
            attributes = i.getValue().getAttributes();
            attributeIterator = attributes.iterator();
            NumOfEncapsulation = 0;

            while (attributeIterator.hasNext()) {
                currentAccessor = attributeIterator.next().getAccessor();
                if (currentAccessor.equals(Accessors.PRIVATE) || currentAccessor.equals(Accessors.PROTECTED))
                    NumOfEncapsulation++;

            }
            result.put(i.getKey(), Double.valueOf(NumOfEncapsulation / attributes.size()));
        }

        return result;
    }

}
