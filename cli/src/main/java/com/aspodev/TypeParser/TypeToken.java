package com.aspodev.TypeParser;

public record TypeToken(String name, String pkg, TypeTokenEnum type) {
    public String getFullName() {
        return (this.pkg() + "." + this.name());
    }
}
