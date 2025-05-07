package com.aspodev.utils;

public class Library {
    private String library,Classs,Interface,annoation,enume;

    public Library(String library,String Classs,String Interface,String annotation,String enume){
        this.annoation = annotation;
        this.Classs = Classs;
        this.Interface = Interface;
        this.library = library;
        this.enume = enume;
    }

    public String getAnnoation() {
        return annoation;
    }

    public String getClasss() { //getclass already exists
        return this.Classs;
    }

    public String getInterface() {
        return Interface;
    }

    public String getLibrary() {
        return library;
    }

    public String getEnume() {
        return enume;
    }

    public void setEnume(String enume) {
        this.enume = enume;
    }

    public void setAnnoation(String annoation) {
        this.annoation = annoation;
    }

    public void setClasss(String classs) {
        Classs = classs;
    }

    public void setInterface(String anInterface) {
        Interface = anInterface;
    }

    public void setLibrary(String library) {
        this.library = library;
    }
}
