package com.aspodev.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * PathResolver is a utility class that resolves and validates file paths.
 * It provides methods to get the absolute path, relative path, and all Java file paths in a directory.
 */
public class PathResolver {
    private final Path path;

    public PathResolver(String path) { // Constructor
        this.path = this.resolvePath(path);
    }

    private boolean isValidPath(Path path) { // Check if the path is valid
        try {
            path.toRealPath();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Path resolvePath(String path) { // Resolve the path
        if (path == null || path.isEmpty()) { // Check if the path is null or empty
            return Paths.get(""); // Return empty path
        }
        Path resolvedPath = Paths.get(path).toAbsolutePath().normalize(); // Get absolute path
        if (!isValidPath(resolvedPath)) { // Check if the path is valid
            throw new IllegalArgumentException("Invalid path: " + path); // Throw exception if invalid
        }
        return resolvedPath; // Return the resolved path
    }

    public List<String> getAllJavaPaths() { // Get all Java file paths in the directory
        List<String> javaFilePaths = new ArrayList<>(); // List to store Java file paths
        try (Stream<Path> paths = Files.walk(path)) { // Walk the file tree
            paths.filter(Files::isRegularFile) // Filter regular files
                    .filter(p -> p.toString().endsWith(".java")) // Filter Java files
                    .forEach(p -> javaFilePaths.add(p.normalize().toString())); // Add Java file paths to the list
        } catch (IOException e) { // Handle IO exception
            e.printStackTrace();
        }
        return javaFilePaths; // Return the list of Java file paths
    }
}