package com.aspodev.cli;

import com.aspodev.DTO.SystemResultDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonWriter {

    // Formatter for timestamped folder names
    private static final DateTimeFormatter FOLDER_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    /**
     * Serializes the DTO and writes it to
     * .aspo/{timestamp}/{projectName}.json
     */
    public static void export(SystemResultDTO dto) throws IOException {
        // 1. Serialize DTO to pretty JSON
        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
        String json = mapper.writeValueAsString(dto);

        // 2. Root .aspo directory
        Path root = Paths.get(".aspo");

        // 3. Timestamp sub-folder
        String timestamp = LocalDateTime.now().format(FOLDER_FORMATTER);
        Path timestampDir = root.resolve(timestamp);

        // 4. Ensure directories exist
        if (Files.notExists(timestampDir)) {
            Files.createDirectories(timestampDir);
        }

        // 5. Target file: projectName.json
        String fileName = dto.getProjectName() + ".json";
        Path target = timestampDir.resolve(fileName);

        // 6. Write JSON (create or overwrite)
        Files.writeString(
                target,
                json,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Exported JSON to " + target.toAbsolutePath());
    }

    /** Safe wrapper around export(...) */
    public static void writeJson(SystemResultDTO dto) {
        try {
            export(dto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
