package com.aspodev.inheritance;

public class Dog extends Mammal {
    public Dog(String name) {
        super(name);
    }

    @Override
    public void speak() {
        System.out.println(name + " says: Woof!");
    }
}
