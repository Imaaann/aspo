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

			Model pmmlModel = pmml.getModels().get(0);
			this.modelEvaluator = ModelEvaluatorFactory.newInstance().newModelEvaluator(pmml, pmmlModel);
			this.modelEvaluator.verify();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to load PMML model");
		}
	}

	public void calculateBug(Map<String, Metrics> result) {
		for (Entry<String, Metrics> entry : result.entrySet()) {
			System.out.println("Target Fields: " + modelEvaluator.getTargetFields());
			System.out.println("Output Fields: " + modelEvaluator.getOutputFields());

			Metrics metrics = entry.getValue();

			Map<String, Object> rawInput = new LinkedHashMap<>();

			for (String feature : features) {
				Double value = metrics.getMetricValue(feature);

				if (value == null) {
					throw new IllegalStateException("Missing value for feature: " + feature);
				}

				rawInput.put(feature, value);
			}

			Map<String, FieldValue> arguments = new LinkedHashMap<>();
			System.out.println("[DEBUG] === Model Expected Inputs ===");
			for (InputField inputField : modelEvaluator.getInputFields()) {
				String name = inputField.getName();
				System.out.println("Expecting: " + name + " (" + inputField.getDataType() + ")");
				if (!rawInput.containsKey(name)) {
					System.err.println("❌ Missing input: " + name);
				}

				Object rawValue = rawInput.get(name);
				FieldValue fieldValue = inputField.prepare(rawValue);
				arguments.put(inputField.getName(), fieldValue);
			}

			System.out.println("=== Actual arguments to evaluate() ===");
			for (Map.Entry<String, FieldValue> arg : arguments.entrySet()) {
				System.out.println(arg.getKey() + " = " + arg.getValue());
			}

			Map<String, ?> prediction = modelEvaluator.evaluate(arguments);

			Object resultObject = prediction.get("Probability");
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
