package com.aspodev.Calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.aspodev.SCAR.Model;

public class SystemCalculator {
	private List<MetricCalculator> calculators;
	private ExecutorService executor;

	public SystemCalculator(int threadCount) {

		// TODO: Add NOC HERE DONT FORGET
		calculators = List.of(new CBOCalculator(), new CFCalculator(), new DACCalculator(), new DAMCalculator(),
				new DITCalculator(), new EDCalculator(), new LCCCalculator(), new LCOM4Calculator(),
				new MITCalculator(), new MPCCalculator(), new NOACalculator(), new NOLMCalculator(),
				new NOMCalculator(), new NOPCalculator(), new NORMCalculator(), new NSMCalculator(),
				new TLOCCalculator());

		executor = Executors.newFixedThreadPool(threadCount);
	}

	public List<Map<String, Double>> runAll(Model SCAR) throws InterruptedException, ExecutionException {
		try {
			List<Future<Map<String, Double>>> futures = calculators.stream()
					.map(c -> executor.submit(() -> c.calculate(SCAR))).toList();

			List<Map<String, Double>> results = new ArrayList<>(futures.size());
			for (Future<Map<String, Double>> f : futures) {
				results.add(f.get());
			}

			return results;
		} finally {
			executor.shutdown();

			if (!executor.awaitTermination(0, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}

		}

	}

	private Map<String, Metrics> normalizeNamedMap(Map<String, Map<String, Double>> nameMap, Set<String> sliceNames) {
		Map<String, Metrics> results = new HashMap<>();
		for (String sliceName : sliceNames) {
			Metrics sliceMetrics = new Metrics();
			for (Map.Entry<String, Map<String, Double>> entry : nameMap.entrySet()) {
				sliceMetrics.insertMetric(entry.getKey(), entry.getValue().get(sliceName));
			}
			results.put(sliceName, sliceMetrics);
		}
		return results;
	}

	public Map<String, Metrics> calculateMetrics(Model SCAR) {
		try {
			List<Map<String, Double>> allResults = runAll(SCAR);

			Map<String, Map<String, Double>> namedResults = new LinkedHashMap<>();
			for (int i = 0; i < calculators.size(); i++) {
				String name = calculators.get(i).getClass().getSimpleName();
				name = name.replace("Calculator", "");
				namedResults.put(name, allResults.get(i));
			}

			// TODO: Bring back PF when NOC is implemented above
			// Handle the other non canonical metrics
			// Current: PF, SIX
			// Map<String, Double> NOC = namedResults.get("NOC");
			Map<String, Double> DIT = namedResults.get("DIT");
			Map<String, Double> NOM = namedResults.get("NOM");
			Map<String, Double> NORM = namedResults.get("NORM");

			// Map<String, Double> PF = new PFCalculator().calculate(SCAR, NOC);
			Map<String, Double> SIX = new SIXCalculator().calculate(DIT, NORM, NOM);
			namedResults.put("SIX", SIX);
			// namedResults.put("PF", PF);

			// Group by class instead of by metrics & Return
			return normalizeNamedMap(namedResults, SCAR.getSliceMap().keySet());
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
			System.err.println("[ERROR] Thread was interrupted");
		} catch (ExecutionException ee) {
			Throwable cause = ee.getCause();
			System.err.println("[ERROR] Calculation Failed" + cause);
			cause.printStackTrace();
		}

		return null;
	}

}
