package com.aspodev.cli;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorBuilder;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.model.PMMLUtil;
import org.jpmml.model.visitors.VisitorBattery;

import com.aspodev.Calculator.Metrics;

public class BugPredictor {
	private ModelEvaluator<?> modelEvaluator;
	private static final List<String> features = List.of("CBO", "CF", "DAC", "DAM", "DIT", "ED", "LCC", "LCOM4", "MIT",
			"NOM", "NOA", "NOC", "NOLM", "NOP", "SIX", "NSM");

	public BugPredictor() {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("model.pmml");
			PMML pmml = PMMLUtil.unmarshal(is);

			VisitorBattery visitors = new VisitorBattery();
			visitors.applyTo(pmml);

			this.modelEvaluator = new ModelEvaluatorBuilder(pmml).build();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("[ERROR] Failed to load PMML model");
		}
	}

	public void calculateBug(Map<String, Metrics> result) {
		for (Entry<String, Metrics> entry : result.entrySet()) {

			Metrics metrics = entry.getValue();

			Map<String, Object> rawInput = new LinkedHashMap<>();

			for (String feature : features) {
				Double value = metrics.getMetricValue(feature);

				if (value == null) {
					throw new IllegalStateException("[ERROR] Missing value for feature: " + feature);
				}

				rawInput.put(feature, value);
			}

			Map<String, FieldValue> arguments = new LinkedHashMap<>();
			for (InputField inputField : modelEvaluator.getInputFields()) {
				String name = inputField.getName();

				if (!rawInput.containsKey(name)) {
					System.err.println("[ERROR] Missing input: " + name);
				}

				Object rawValue = rawInput.get(name);
				FieldValue fieldValue = inputField.prepare(rawValue);
				arguments.put(inputField.getName(), fieldValue);
			}

			Map<String, ?> prediction = modelEvaluator.evaluate(arguments);

			Object resultObject = prediction.get("probability_defect");
			double bugProbability = 0.0;

			if (resultObject instanceof Map<?, ?> map) {
				Object prob = map.get("1");
				if (prob instanceof Number) {
					bugProbability = ((Number) prob).doubleValue();

				}
			} else if (resultObject instanceof Number) {
				bugProbability = ((Number) resultObject).doubleValue();
			}

			metrics.insertMetric("BUGP", bugProbability);
		}
	}

}
