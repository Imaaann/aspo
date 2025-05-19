package com.aspodev.SCAR;

public enum Modifier {
	ABSTRACT, SYNCHRONIZED, TRANSIENT, FINAL, STATIC, STRICT_FP, VOLATILE, NON_SEALED, SEALED, DEFAULT, NONE;

	public static Modifier convert(String str) {
		return switch (str) {
		case "abstract":
			yield ABSTRACT;
		case "synchronized":
			yield SYNCHRONIZED;
		case "transient":
			yield TRANSIENT;
		case "final":
			yield FINAL;
		case "static":
			yield STATIC;
		case "strictfp":
			yield STRICT_FP;
		case "volatile":
			yield VOLATILE;
		case "non-sealed":
			yield NON_SEALED;
		case "sealed":
			yield SEALED;
		case "default":
			yield DEFAULT;
		default:
			yield NONE;
		};
	}
}
