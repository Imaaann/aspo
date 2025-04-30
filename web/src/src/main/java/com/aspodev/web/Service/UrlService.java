package com.aspodev.web.Service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UrlService {

    private String storedUrl;

    public String getUrl() throws IOException {
        if (storedUrl == null) {
            throw new IOException("No URL stored");
        }
        return storedUrl;
    }

    public void saveUrl(String url) throws IOException {
        if (url == null || url.isBlank()) {
            throw new IOException("URL cannot be null or empty");
        }
        this.storedUrl = url;
    }
}