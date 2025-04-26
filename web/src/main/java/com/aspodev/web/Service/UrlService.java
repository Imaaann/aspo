package com.aspodev.web.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UrlService {
    private final ObjectMapper objectMapper;
    private final String filePath;


    public UrlService(ObjectMapper objectMapper, 
                     @Value("${url.storage.path:url.json}") String filePath) {
        this.objectMapper = objectMapper;
        this.filePath = filePath;
    }

    public void saveUrl(String url) throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("url", url);
        try {
            objectMapper.writeValue(new File(filePath), data);
        } catch (IOException e) {
            throw new IOException("Failed to save URL to file", e);
        }
    }

    public String getUrl() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try {
            Map<?, ?> data = objectMapper.readValue(file, Map.class);
            return (String) data.get("url");
        } catch (IOException e) {
            throw new IOException("Failed to read URL from file", e);
        }
    }
}
