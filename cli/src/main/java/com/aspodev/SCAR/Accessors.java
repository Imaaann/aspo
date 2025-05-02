package com.aspodev.SCAR;

public enum Accessors {
	PUBLIC, DEFAULT, PROTECTED, PRIVATE;

	public static Accessors convert(String str) {
		return switch (str) {
		case "public":
			yield PUBLIC;
		case "protected":
			yield PROTECTED;
		case "private":
			yield PRIVATE;
		default:
			yield DEFAULT;
		};
	}
}