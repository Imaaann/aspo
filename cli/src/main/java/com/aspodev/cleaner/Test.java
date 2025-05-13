package com.aspodev.cleaner;

public class Test {
	public static void main(String[] args) {
		// Anonymous class implementing an interface
		Runnable r = new Runnable() {
			@Override
			public void run() {
				System.out.println("Running anonymously");
			}
		};
		r.run();

		process(new Object() {
			void greet() {
				System.out.println("Hello from inline anonymous class");
			}
		});

		// Anonymous class extending a concrete class
		Thread t = new Thread("CustomThread") {
			@Override
			public void run() {
				System.out.println("Custom thread running: " + getName());
			}
		};
		t.start();

		// Anonymous class passed directly as a method argument
	}

	public static void process(Object obj) {
		System.out.println("Processing: " + obj.getClass().getSimpleName());
	}
}
