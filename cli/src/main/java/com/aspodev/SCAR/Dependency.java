package com.aspodev.SCAR;

public class Dependency {
	private final String name;
	private String callerType;

	public Dependency(String name, String callerType) {
		this.name = name;
		this.callerType = callerType;
	}

	public String getName() {
		return this.name;
	}

	public String getCallerType() {
		return this.callerType;
	}

	public void setCallerType(String callerType) {
		this.callerType = callerType;
	}

	public String toString() {
		return "\t\t\t-\t" + name + "---" + callerType + "\n";
	}
}
