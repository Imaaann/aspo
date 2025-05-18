package com.aspodev.inheritance;

public class TalkingParrot extends Parrot {
    public TalkingParrot(String name) {
        super(name);
    }

    @Override
    public void speak() {
        System.out.println(name + " says: Hello! I'm a talking parrot!");
    }
}
