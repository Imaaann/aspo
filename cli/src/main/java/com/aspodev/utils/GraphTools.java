package com.aspodev.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
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

	public static <N> boolean isReachable(Map<N, List<N>> graph, N start, N target) {
		if (start.equals(target))
			return true;

		Set<N> visited = new HashSet<>();
		Queue<N> queue = new ArrayDeque<>();
		visited.add(start);
		queue.add(start);

		while (!queue.isEmpty()) {
			N current = queue.poll();
			for (N neighbor : graph.getOrDefault(current, List.of())) {
				if (!visited.contains(neighbor)) {
					if (neighbor.equals(target)) {
						return true;
					}
					visited.add(neighbor);
					queue.add(neighbor);
				}
			}
		}
		return false;
	}

	/**
	 * Precompute, for each node, the full set of nodes reachable from it.
	 * 
	 * @param graph adjacency-list graph
	 * @param <N>   node type
	 * @return map from each node → set of all nodes reachable from it
	 */
	public static <N> Map<N, Set<N>> computeReachabilityMap(Map<N, List<N>> graph) {
		Map<N, Set<N>> reachableFrom = new HashMap<>();

		for (N start : graph.keySet()) {
			// If we've already computed it (e.g. due to SCC compression), skip.
			if (reachableFrom.containsKey(start))
				continue;

			// Do one BFS/DFS from 'start'
			Set<N> visited = new HashSet<>();
			Deque<N> stack = new ArrayDeque<>();
			visited.add(start);
			stack.push(start);

			while (!stack.isEmpty()) {
				N curr = stack.pop();
				for (N nbr : graph.getOrDefault(curr, List.of())) {
					if (visited.add(nbr)) {
						stack.push(nbr);
					}
				}
			}

			// Remove self if you don't count m→m
			visited.remove(start);

			reachableFrom.put(start, visited);
		}

		return reachableFrom;
	}

}
