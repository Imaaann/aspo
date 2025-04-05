package com.aspodev.TypeParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aspodev.cleaner.Cleaner;

public class TypeSpace {
    private HashMap<String, List<TypeToken>> typeSpace;

    public TypeSpace(List<Path> paths) {
        typeSpace = new HashMap<>();
        StringBuilder contents;
        for (Path path : paths) {
            try {

                contents = new StringBuilder(Files.readString(path));
                
                Cleaner.cleanFile(contents);
                
                List<TypeToken> typesFound = TypeFinder.findAllTypes(contents);

                String packageName = !typesFound.isEmpty() ? typesFound.get(0).pkg() : "NONE";
                typeSpace.computeIfAbsent(packageName, (k) -> new ArrayList<>()).addAll(typesFound);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // Temporary
    public HashMap<String, List<TypeToken>> getMap() {
        return typeSpace;
    }
    
    public List<TypeToken> findPackageTypes(String pkg) {
        return null;
    }

    public boolean isType(String token, String pkg) {
        return false;
    }

}
