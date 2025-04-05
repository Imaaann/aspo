package com.aspodev.TypeParser.TypeTree;

import java.util.ArrayList;
import java.util.List;

import com.aspodev.TypeParser.TypeToken;

public class PackageComponent implements TypeTreeComponent {
    private ArrayList<TypeTreeComponent> children;

    public PackageComponent(List<TypeToken> childrenList) {
        children = new ArrayList<>();

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
}
