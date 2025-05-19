package com.aspodev.inheritance;

public class Cat extends Mammal {
    public Cat(String name) {
        super(name);
    }

    @Override
    public void speak() {
        System.out.println(name + " says: Meow!");
    }
}
