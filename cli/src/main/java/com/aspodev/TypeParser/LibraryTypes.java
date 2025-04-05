package com.aspodev.TypeParser;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LibraryTypes(@JsonProperty("class") List<String> classes,
		@JsonProperty("interface") List<String> interfaces,
		@JsonProperty("enum") List<String> enums,
		@JsonProperty("annotation") List<String> annotations) {
}
