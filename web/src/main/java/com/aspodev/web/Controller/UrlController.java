package com.aspodev.web.Controller;

import com.aspodev.web.Service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/calculate/url")  
public class UrlController {  

    private final UrlService urlService;

    // Use constructor injection
    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<?> setUrl(@RequestParam String url) {
        try {
            urlService.saveUrl(url);
            return ResponseEntity.ok(Map.of(
                "Here where we need to print the files name",
                    ""
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "Error saving URL", e.getMessage()
            ));
        }
    }

    @GetMapping
    public ResponseEntity<?> getUrl() {
        try {
            String url = urlService.getUrl();
            if (url == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "not_found", 
                    "No URL has been stored yet"
                ));
            }
            return ResponseEntity.ok(Map.of(
                "url",
                url
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "Error fetching URL",
                e.getMessage()
            ));
        }
    }
}