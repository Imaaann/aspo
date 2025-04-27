package com.aspodev.web.Service;

import com.aspodev.web.model.Url;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

@Service
public class UrlService {

    private final String file = "url.json";
    public String getUrl() throws IOException {
        try {
            ObjectMapper OM = new ObjectMapper();
            InputStream input = new ClassPathResource("/data/"+"files.json").getInputStream();
            return OM.readValue(input, Url.class).getUrl();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}