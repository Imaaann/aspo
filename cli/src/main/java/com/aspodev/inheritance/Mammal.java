package com.aspodev.inheritance;

public abstract class Mammal extends Animal {
    public Mammal(String name) {
        super(name);
    }

    public void walk() {
        System.out.println(name + " is walking on legs.");
    }
}
