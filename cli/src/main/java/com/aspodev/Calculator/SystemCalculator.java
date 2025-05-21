package com.aspodev.Calculator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

		calculators = List.of(new CBOCalculator(), new CFCalculator(), new DACCalculator(), new DAMCalculator(),
				new DITCalculator(), new EDCalculator(), new LCCCalculator(), new LCOM4Calculator(),
				new MITCalculator(), new MPCCalculator(), new NOACalculator(), new NOLMCalculator(),
				new NOMCalculator(), new NOPCalculator(), new NORMCalculator(), new NSMCalculator(),
				new TLOCCalculator(), new NOCCalculator(), new RFCCalculator());

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

	/**
	 * Transpose a map of metricName → (sliceName → metricValue) into sliceName →
	 * Metrics Only the actual entries in each metric map get inserted.
	 */
	private Map<String, Metrics> normalizeNamedMap(Map<String, Map<String, Double>> metricMap) {
		Map<String, Metrics> results = new LinkedHashMap<>();

		for (var metricEntry : metricMap.entrySet()) {
			String metricName = metricEntry.getKey(); // e.g. "DIT"
			Map<String, Double> sliceValues = metricEntry.getValue();

			for (var sliceEntry : sliceValues.entrySet()) {
				String sliceName = sliceEntry.getKey(); // e.g. "MyClass"
				Double value = sliceEntry.getValue(); // maybe NaN, maybe real

				// if you want to drop NaNs here:
				if (value != null && value.isNaN()) {
					System.out.println("[WARN] NaN for metric " + metricName + " on slice " + sliceName);
					continue;
				}

				// get-or-create the Metrics object for this slice
				Metrics m = results.computeIfAbsent(sliceName, k -> new Metrics());
				m.insertMetric(metricName, value);
			}
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

			Map<String, Double> NOC = namedResults.get("NOC");
			Map<String, Double> DIT = namedResults.get("DIT");
			Map<String, Double> NOM = namedResults.get("NOM");
			Map<String, Double> NORM = namedResults.get("NORM");

			Map<String, Double> PF = new PFCalculator().calculate(SCAR, NOC);
			Map<String, Double> SIX = new SIXCalculator().calculate(DIT, NORM, NOM);
			namedResults.put("SIX", SIX);
			namedResults.put("PF", PF);

			// Group by class instead of by metrics & Return
			return normalizeNamedMap(namedResults);
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
