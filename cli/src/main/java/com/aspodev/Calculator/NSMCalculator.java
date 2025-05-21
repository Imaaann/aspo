package com.aspodev.Calculator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.aspodev.SCAR.Attribute;
import com.aspodev.SCAR.Method;
import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;
import com.aspodev.SCAR.Modifier;

public class NSMCalculator implements MetricCalculator {

    @Override
    public Map<String, Double> calculate(Model SCAR) {
        Map<String, Double> result = new HashMap<>();
        Map<String, Slice> slicesMap = SCAR.getSliceMap();
        int NumOfStatic = 0;
        Set<Attribute> attributes;
        Set<Method> methods;
        Iterator<Method> methodIterator;
        Iterator<Attribute> attributeIterator;
        Method currentMethod;
        Attribute currenAttribute;

        for (Map.Entry<String, Slice> i : slicesMap.entrySet()) {
            NumOfStatic = 0;
            methods = i.getValue().getMethods();
            methodIterator = methods.iterator();

            while (methodIterator.hasNext()) {
                currentMethod = methodIterator.next();
                if (currentMethod.getModifiers().contains(Modifier.STATIC))
                    NumOfStatic++;
            }

            attributes = i.getValue().getAttributes();
            attributeIterator = attributes.iterator();

            while (attributeIterator.hasNext()) {
                currenAttribute = attributeIterator.next();
                if (currenAttribute.getModifiers().contains(Modifier.STATIC))
                    NumOfStatic++;
            }
            result.put(i.getKey(), Double.valueOf(NumOfStatic));

        }

        return result;
    }

}