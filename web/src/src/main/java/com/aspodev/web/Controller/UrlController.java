package com.aspodev.web.Controller;

import com.aspodev.web.Exception.UrlException;
import com.aspodev.web.Service.DockerService;
import com.aspodev.web.Service.UrlService;
import com.aspodev.web.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlService urlService;
    private final DockerService dockerService;

    @Autowired
    public UrlController(UrlService urlService, DockerService dockerService) {
        this.urlService = urlService;
        this.dockerService = dockerService;
    }

    @GetMapping("/url")
    public ResponseEntity<Url> getUrl() {
        try {
            String url = urlService.getUrl();
            return ResponseEntity.ok(new Url(url));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Url("Error retrieving URL: " + e.getMessage()));
        }
    }

    @PostMapping("/url")
    public ResponseEntity<Url> postUrl(@RequestBody Url url) {
        try {
            if (url == null || url.getUrl() == null || url.getUrl().isBlank()) {
                return ResponseEntity.badRequest().body(new Url("URL is required"));
            }
            urlService.saveUrl(url.getUrl());
            return ResponseEntity.ok(url);
        } catch (IOException | UrlException e) {
            return ResponseEntity.internalServerError().body(new Url("Error saving URL: " + e.getMessage()));
        }
    }

    @PostMapping("/files")
    public ResponseEntity<List<String>> postFiles(
            @RequestParam String containerId,
            @RequestParam(defaultValue = "/CloneDir") String path) {
        try {
            List<String> files = dockerService.getClonedFiles(containerId, path);
            return ResponseEntity.ok(files);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.internalServerError().body(List.of("Error listing files: " + e.getMessage()));
        }
    }
}