package com.aspodev.Calculator;

import java.util.Map;

import com.aspodev.SCAR.Method;
import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;

public class MHFCalculator {

    public Double calculate(Model SCARModel) {
        int visibleCount = 0;
        int totalMethods = 0;

        Map<String, Slice> slicesMap = SCARModel.getSliceMap();
        for (Slice slice : slicesMap.values()) {
            for (Method method : slice.getMethods()) {
                totalMethods++;
                if (isVisible(slice, method, slicesMap)) {
                    visibleCount++;
                }
            }
        }

        return totalMethods == 0 ? 0.0 : (1.0 - ((double) visibleCount / totalMethods));
    }

    private boolean isVisible(Slice slice, Method method, Map<String, Slice> slicesMap) {
        switch (method.getAccessor()) {
        case PUBLIC:
            return true;
        case PRIVATE:
            return false;
        case PROTECTED:
            return hasSubclass(slice, slicesMap) || inSamePackage(slice, slicesMap);
        case DEFAULT:
            return inSamePackage(slice, slicesMap);
        default:
            return false;
        }
    }

    private boolean hasSubclass(Slice slice, Map<String, Slice> slicesMap) {
        String className = slice.getMetaData().getFullName();
        for (Slice other : slicesMap.values()) {
            String parent = other.getParentName();
            if (parent != null && parent.equals(className)) {
                return true;
            }
        }
        return false;
    }

    private boolean inSamePackage(Slice slice, Map<String, Slice> slicesMap) {
        String pkg = slice.getMetaData().pkg();
        String className = slice.getMetaData().getFullName();
        for (Slice other : slicesMap.values()) {
            if (!other.getMetaData().getFullName().equals(className) && other.getMetaData().pkg().equals(pkg)) {
                return true;
            }
        }
        return false;
    }
}
