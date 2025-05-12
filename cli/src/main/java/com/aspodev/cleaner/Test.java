package com.aspodev.cleaner;

import java.util.ArrayList;

public enum Test {

	TEST((int) Math.pow(3, 3)) {
	},
	TEST2(4), TEST3(1), TEST5(5), TEST4(4) {
		private int another;
	};

	private Test(int real) {
		value = real;
		ArrayList<Integer> ea = new ArrayList<>();
		ea.add(33);
		switch (ea.get(0)) {
		case 1 -> System.out.println("eee");
		case 2, 3 -> {

		}
		}
	}

	private int value;

}
