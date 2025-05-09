package com.aspodev.cleaner;

public class Test {

	static {
		System.out.println("[WARN] You're using a static thing fucker");
	}

	{
		System.out.println("[WARN] You're using an intialization block dumass");
	}

	public <T> Test(T real) {
		System.out.println(real);
	}
}
