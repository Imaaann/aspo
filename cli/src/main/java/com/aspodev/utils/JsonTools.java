package com.aspodev.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;



public class JsonTools {

	/**
	 * @param <T>              type for object mapping, use records here with
	 *                         the @JsonProperty
	 * @param jsonResourcePath Resource path for the json file root is set to
	 *                         "src/main/resources/"
	 * @param clazz            type refrence for the return type use type.class to
	 *                         get it
	 * @return The JSON data mapped to your object
	 * @throws IOException if the resource wasn't found
	 */
	public static <T> T readJsonObject(String jsonResourcePath, Class<T> clazz) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		InputStream is = JsonTools.class.getResourceAsStream(jsonResourcePath);

		if (is == null)
			throw new IOException("Resource not found!");

		return objectMapper.readValue(is, clazz);
	}
}
