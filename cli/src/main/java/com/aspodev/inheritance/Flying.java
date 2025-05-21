package com.aspodev.inheritance;

public interface Flying {
	public default void fly() {
		System.out.println("FLY YEAAAH");
	}
}
