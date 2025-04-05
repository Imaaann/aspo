package com.aspodev.TypeParser.TypeTree;

import com.aspodev.TypeParser.TypeToken;

public class TypeComponent implements TypeTreeComponent {
    private TypeToken type;

    public TypeComponent(TypeToken typeToken) {
        this.type = typeToken;
    }

    public boolean checkType(String token) {
        return type.name() == token;
    }

    @Override
    public String getName() {
        return type.name();
    }

    @Override
    public String getType() {
        return type.type().toString();
    }
    
}
