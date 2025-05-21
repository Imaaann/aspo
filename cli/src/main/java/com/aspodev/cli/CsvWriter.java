package com.aspodev.cli;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.aspodev.Calculator.Metrics;

public class CsvWriter {
	// Base directory
	private static final String BASE_DIR = "./.aspo";

	// Timestamp formatter for folder names
	private static final DateTimeFormatter FOLDER_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

	/**
	 * Writes the given metrics map to CSV at ./​.aspo/{current-date}/out.csv
	 *
	 * @param data map from sliceName → Metrics
	 * @throws IOException if an IO error occurs
	 */
	public static void writeCsv(Map<String, Metrics> data) throws IOException {
		// 1) Determine current date directory
		String timestamp = LocalDateTime.now().format(FOLDER_FORMATTER);
		Path dirPath = Path.of(BASE_DIR, timestamp);
		Files.createDirectories(dirPath);

		// 2) Collect all metric names (union) in sorted order for consistent columns
		Set<String> metricNames = new TreeSet<>();
		for (Metrics m : data.values()) {
			metricNames.addAll(m.keySet());
		}

		// 3) Build header: first "name", then each metric
		List<String> headers = new java.util.ArrayList<>();
		headers.add("name");
		headers.addAll(metricNames);

		// 4) CSVFormat with header
		CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(headers.toArray(new String[0]))
				.setSkipHeaderRecord(false).build();

		// 5) Create printer and write rows
		Path csvPath = dirPath.resolve("out.csv");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath.toFile()));
				CSVPrinter printer = new CSVPrinter(writer, format)) {
			for (Map.Entry<String, Metrics> entry : data.entrySet()) {
				String name = entry.getKey();
				Metrics m = entry.getValue();

				// Build record: first sliceName, then each metric's value (or blank if missing)
				List<String> record = new java.util.ArrayList<>();
				record.add(name);
				for (String metric : metricNames) {
					Double v = m.getMetricValue(metric);
					record.add(v != null ? v.toString() : "");
				}
				printer.printRecord(record);
			}
			printer.flush();
		}

		System.out.println("Written CSV to: " + csvPath.toAbsolutePath());
	}
}
