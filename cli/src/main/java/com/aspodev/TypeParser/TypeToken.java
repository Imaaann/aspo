package com.aspodev.TypeParser;

import java.util.Objects;

public class TypeToken{
    private final String name;
    private final String pkg;
    private final TypeTokenEnum type;

    public TypeToken (String name, String pkg, TypeTokenEnum type ){
        this.name = name;
        this.pkg = pkg;
        this.type = type;
    }
    public String name(){
        return this.name;
    }

    public String pkg(){
        return pkg;
    }

    public TypeTokenEnum type(){
        return this.type;
    }

    public String getFullName() {
        return (this.pkg() + "." + this.name());
    }

    public String toString() {
        return getFullName() + "." + this.type;
    }
    @Override
    public boolean equals(Object o){
        if( o instanceof TypeToken){
            TypeToken typetoken = (TypeToken)o;
            return typetoken.name().equals(this.name());
        } 
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
