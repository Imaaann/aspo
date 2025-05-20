package com.aspodev.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GraphTools {
	/**
	 * Prints any adjacency‐list graph of homogeneous node type. Example: Map<Node,
	 * List<Node>>
	 *
	 * @param graph      map from a node to its outgoing neighbors
	 * @param toStringFn (optional) how to render each node; use Object::toString if
	 *                   null
	 * @param <N>        node type
	 */
	public static <N> void displayGraph(Map<N, List<N>> graph, Function<N, String> toStringFn) {
		Function<N, String> fmt = (toStringFn != null) ? toStringFn : Object::toString;

		if (graph == null || graph.isEmpty()) {
			System.out.println("(graph is empty)");
			return;
		}

		graph.forEach((node, neighbors) -> {
			System.out.println(fmt.apply(node) + ":");
			if (neighbors == null || neighbors.isEmpty()) {
				System.out.println("  (no edges)");
			} else {
				neighbors.forEach(n -> System.out.println("  - " + fmt.apply(n)));
			}
			System.out.println();
		});
	}

	/**
	 * Prints any adjacency‐list graph, possibly with heterogeneous origin/target
	 * types. Example: Map<From, List<To>>
	 *
	 * @param graph        map from origin nodes to lists of target nodes
	 * @param fromToString how to render origin nodes; Object::toString if null
	 * @param toToString   how to render target nodes; Object::toString if null
	 * @param <F>          origin node type
	 * @param <T>          target node type
	 */
	public static <F, T> void displayGraph(Map<F, List<T>> graph, Function<F, String> fromToString,
			Function<T, String> toToString) {
		Function<F, String> fmtFrom = (fromToString != null) ? fromToString : Object::toString;
		Function<T, String> fmtTo = (toToString != null) ? toToString : Object::toString;

		if (graph == null || graph.isEmpty()) {
			System.out.println("(graph is empty)");
			return;
		}

		graph.forEach((from, tos) -> {
			System.out.println(fmtFrom.apply(from) + ":");
			if (tos == null || tos.isEmpty()) {
				System.out.println("  (no edges)");
			} else {
				tos.forEach(to -> System.out.println("  - " + fmtTo.apply(to)));
			}
			System.out.println();
		});
	}
}
