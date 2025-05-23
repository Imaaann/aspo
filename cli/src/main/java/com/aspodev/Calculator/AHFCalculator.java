package com.aspodev.Calculator;

import java.util.Map;

import com.aspodev.SCAR.Attribute;
import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;
import com.aspodev.SCAR.Accessors;

public class AHFCalculator {

    public Double calculate(Model SCARModel) {
        Double AHF = 0.0;
        Map<String, Slice> slicesMap = SCARModel.getSliceMap();
        int numOfAttributes = 0;
        for (Map.Entry<String, Slice> entry : slicesMap.entrySet()) {
            Slice slice = entry.getValue();
            for (Attribute attribute : slice.getAttributes()) {
                boolean isInnerClass = slice.getOuterClass() != null;
                boolean isOuterClasschecked = false;
                switch (attribute.getAccessor()) {
                    case PUBLIC:
                        if (isInnerClass && !isOuterClasschecked) {
                            AHF += checkWithOuterClass(slice, slicesMap);
                            isOuterClasschecked = true;
                        }
                        AHF += 1;
                        break;
                    case PROTECTED:
                        if (isInnerClass && !isOuterClasschecked) {
                            AHF += checkWithOuterClass(slice, slicesMap);
                            isOuterClasschecked = true;
                        }
                        AHF += checkWithChildren(slice, slicesMap);
                        AHF += checkWithPkg(slice, slicesMap);
                        break;
                    case PRIVATE:
                        if (isInnerClass && !isOuterClasschecked) {
                            AHF += checkWithOuterClass(slice, slicesMap);
                            isOuterClasschecked = true;
                            AHF += 1 / slicesMap.size();
                        }
                        break;
                    case DEFAULT:
                        if (isInnerClass && !isOuterClasschecked) {
                            AHF += checkWithOuterClass(slice, slicesMap);
                            isOuterClasschecked = true;
                        }
                        AHF += checkWithPkg(slice, slicesMap);
                        break;
                }
            }
            numOfAttributes += slice.getAttributes().size();
        }

        return Double.valueOf((1 - AHF) / numOfAttributes);
    }

    private Double checkWithChildren(Slice slice, Map<String, Slice> slicesMap) {
        Double AHF = 0.0;
        String pkg = slice.getMetaData().pkg();
        String className = slice.getMetaData().getFullName();
        for (Map.Entry<String, Slice> entry : slicesMap.entrySet()) {
            Slice externaSlice = entry.getValue();
            if (!(externaSlice.getMetaData().getFullName().equals(className))
                    && !externaSlice.getMetaData().pkg().equals(pkg)) {

                if (entry.getValue().getParentName().equals(className))
                    AHF++;

            }
        }
        AHF = AHF / (slicesMap.size() - 1);
        return AHF;
    }

    private Double checkWithPkg(Slice slice, Map<String, Slice> slicesMap) {
        Double AHF = 0.0;
        String pkg = slice.getMetaData().pkg();
        String className = slice.getMetaData().getFullName();
        for (Map.Entry<String, Slice> entry : slicesMap.entrySet()) {
            Slice externaSlice = entry.getValue();
            if (!(externaSlice.getMetaData().getFullName().equals(className))
                    && externaSlice.getMetaData().pkg().equals(pkg))
                AHF++;

        }
        AHF = AHF / (slicesMap.size() - 1);
        return Double.valueOf(AHF);
    }

    private Double checkWithOuterClass(Slice slice, Map<String, Slice> slicesMap) {
        Double AHF = 0.0;
        String outerClass = slice.getOuterClass();
        for (Attribute attribute : slicesMap.get(outerClass).getAttributes()) {
            if (attribute.getAccessor() == Accessors.PRIVATE) {
                AHF++;
            }
        }

        AHF = AHF / (slicesMap.size() - 1);
        return Double.valueOf(AHF);
    }
}
