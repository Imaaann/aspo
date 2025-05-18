package com.aspodev.inheritance;

public abstract class Animal {
    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    public abstract void speak();

    public void eat() {
        System.out.println(name + " is eating.");
    }
}
