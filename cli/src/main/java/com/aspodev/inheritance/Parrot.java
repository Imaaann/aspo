package com.aspodev.inheritance;

public class Parrot extends Bird {
    public Parrot(String name) {
        super(name);
    }

    @Override
    public void speak() {
        System.out.println(name + " says: Squawk!");
    }
}
