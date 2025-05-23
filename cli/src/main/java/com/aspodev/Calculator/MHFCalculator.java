package com.aspodev.Calculator;

import java.util.Map;

import com.aspodev.SCAR.Method;
import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;
import com.aspodev.SCAR.Accessors;

public class MHFCalculator {

    public Double calculate(Model SCARModel) {
        Double MHF = 0.0;
        Map<String, Slice> slicesMap = SCARModel.getSliceMap();
        int numOfMethods = 0;
        for (Map.Entry<String, Slice> entry : slicesMap.entrySet()) {
            Slice slice = entry.getValue();
            for (Method method : slice.getMethods()) {
                boolean isInnerClass = slice.getOuterClass() != null;
                boolean isOuterClasschecked = false;
                switch (method.getAccessor()) {
                    case PUBLIC:
                        if (isInnerClass && !isOuterClasschecked) {
                            MHF += checkWithOuterClass(slice, slicesMap);
                            isOuterClasschecked = true;
                        }
                        MHF += 1;
                        break;
                    case PROTECTED:
                        if (isInnerClass && !isOuterClasschecked) {
                            MHF += checkWithOuterClass(slice, slicesMap);
                            isOuterClasschecked = true;
                        }
                        MHF += checkWithChildren(slice, slicesMap);
                        MHF += checkWithPkg(slice, slicesMap);
                        break;
                    case PRIVATE:
                        if (isInnerClass && !isOuterClasschecked) {
                            MHF += checkWithOuterClass(slice, slicesMap);
                            isOuterClasschecked = true;
                            MHF += 1 / slicesMap.size();
                        }
                        break;
                    case DEFAULT:
                        if (isInnerClass && !isOuterClasschecked) {
                            MHF += checkWithOuterClass(slice, slicesMap);
                            isOuterClasschecked = true;
                        }
                        MHF += checkWithPkg(slice, slicesMap);
                        break;
                }
            }
            numOfMethods += slice.getMethods().size();
        }

        return Double.valueOf((1 - MHF) / numOfMethods);
    }

    private Double checkWithChildren(Slice slice, Map<String, Slice> slicesMap) {
        Double MHF = 0.0;
        String pkg = slice.getMetaData().pkg();
        String className = slice.getMetaData().getFullName();
        for (Map.Entry<String, Slice> entry : slicesMap.entrySet()) {
            Slice externaSlice = entry.getValue();
            if (!(externaSlice.getMetaData().getFullName().equals(className))
                    && !externaSlice.getMetaData().pkg().equals(pkg)) {

                if (entry.getValue().getParentName().equals(className))
                    MHF++;

            }
        }
        MHF = MHF / (slicesMap.size() - 1);
        return Double.valueOf(MHF);
    }

    private Double checkWithPkg(Slice slice, Map<String, Slice> slicesMap) {
        Double MHF = 0.0;
        String pkg = slice.getMetaData().pkg();
        String className = slice.getMetaData().getFullName();
        for (Map.Entry<String, Slice> entry : slicesMap.entrySet()) {
            Slice externaSlice = entry.getValue();
            if (!(externaSlice.getMetaData().getFullName().equals(className))
                    && externaSlice.getMetaData().pkg().equals(pkg))
                MHF++;

        }
        MHF = MHF / (slicesMap.size() - 1);
        return Double.valueOf(MHF);
    }

    private Double checkWithOuterClass(Slice slice, Map<String, Slice> slicesMap) {
        Double MHF = 0.0;
        String outerClass = slice.getOuterClass();
        for (Method method : slicesMap.get(outerClass).getMethods()) {
            if (method.getAccessor() == Accessors.PRIVATE) {
                MHF++;
            }
        }

        MHF = MHF / (slicesMap.size() - 1);
        return Double.valueOf(MHF);
    }
}
