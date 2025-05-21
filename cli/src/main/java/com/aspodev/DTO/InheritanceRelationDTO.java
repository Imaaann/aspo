package com.aspodev.DTO;

public class InheritanceRelationDTO {
    private String name;
    private String type;

    public InheritanceRelationDTO(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
