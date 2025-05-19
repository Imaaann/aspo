package com.aspodev.inheritance;

public class Bird extends Animal {
    public Bird(String name) {
        super(name);
    }

    public void fly() {
        System.out.println(name + " is flying.");
    }

    @Override
    public void speak() {
        System.out.println(name + " chirps.");
    }
}