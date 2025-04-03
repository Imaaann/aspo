package com.aspodev.cli;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathResolver {

    private static boolean isValidPath(Path path) { // this method checks if a path is valid
        try {
            path.toRealPath(); // try to convert the path to a real path
            return true;
        } catch (Exception e) {
            return false; // if an exception is thrown, the path is invalid
        }
    }

    public static Path resolvePath(String path) { // this method is used to resolve the path to a file or directory
        if (path == null || path.isEmpty()) {
            return Paths.get(""); // if the path is null or empty, return an empty path
        }
        Path resolvedPath = Paths.get(path).normalize(); // normalize the path to remove redundant elements
        if (!isValidPath(resolvedPath)) {
            throw new IllegalArgumentException("Invalid path: " + path); // throw an exception for invalid paths
        }
        return resolvedPath;
    }

    public static String getAbsolutePath(String path) { // this method is used to resolve the path to a file or directory and return it as an absolute path
        return resolvePath(path).toAbsolutePath().toString();
    }

    public static String getRelativePath(String path) { // this method is used to resolve the path to a file or directory and return it as a relative path
        return resolvePath(path).toString();
    }

}
