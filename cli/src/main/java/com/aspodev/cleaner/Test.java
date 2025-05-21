package com.aspodev.cleaner;

import static java.lang.System.*;
import static java.lang.Math.pow;
import com.aspodev.tokenizer.Tokenizer;
import com.aspodev.inheritance.Parrot;

import java.util.*;

public class Test {
	public static void main(String[] args) {

		Map<? extends Comparable<? super Parrot>, List<? super List<? extends Tokenizer>>> insaneMap = new HashMap<>();
		insaneMap.isEmpty();
		currentTimeMillis();
		pow(3, 3);

		// Anonymous class implementing an interface
		Runnable r = new Runnable() {
			@Override
			public void run() {
				System.out.println("Running anonymously");
			}
		};
		r.run();

		process(new Object() {

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
