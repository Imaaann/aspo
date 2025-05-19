package com.aspodev.inheritance;

public abstract class Bird extends Animal {
    public Bird(String name) {
        super(name);
    }

    public void fly() {
        System.out.println(name + " is flying.");
    }
}