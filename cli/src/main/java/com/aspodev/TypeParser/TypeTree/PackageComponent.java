package com.aspodev.TypeParser.TypeTree;

import java.util.ArrayList;
import java.util.List;

import com.aspodev.TypeParser.TypeToken;

public class PackageComponent implements TypeTreeComponent {
    private ArrayList<TypeTreeComponent> children;
    private String packageName;

    public PackageComponent(List<TypeToken> childrenList) {
        children = new ArrayList<>();

        if (childrenList.size() != 0) {
            packageName = childrenList.get(0).pkg();
        } else {
            packageName = "NONE";
        }

        for (TypeToken typeToken : childrenList) {
            children.add(new TypeComponent(typeToken));
        }

    }

    public boolean checkType(String token) {
        for (TypeTreeComponent typeComponent : children) {
            if (!typeComponent.checkType(token)) return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return packageName;
    }

    @Override
    public String getType() {
        return "PACKAGE";
    }
}
