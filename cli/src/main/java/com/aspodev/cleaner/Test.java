package com.aspodev.cleaner;

public enum Test {

	TEST((int) Math.pow(3, 3)) {
	},
	TEST2(4), TEST3(1), TEST5(5), TEST4(4) {
		private int another;
	};

	private Test(int real) {
		value = real;
	}

	private int value;

}
