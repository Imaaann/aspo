package com.aspodev.Metrics.DIT;

import java.nio.file.Path;
import java.util.HashMap;
import com.aspodev.tokenizer.Tokenizer;

public class DIT {
    private static int dit = 1;
    private Tokenizer tokenizer;
    private Path file_Path;

    public int getDit() {
        return CalculateDIT();
    }

    public DIT(Path file_Path) {
        this.file_Path = file_Path;
        this.tokenizer = new Tokenizer(file_Path);
    }

    private int CalculateDIT() {
        HashMap<String, Boolean> classTree = tokenizer.getCLASS_TREE();
        for(String Class : classTree.keySet()){
            if(classTree.get(Class) != null && classTree.get(Class)) {
                dit++;
            }
        }
        return dit;
    }
}
