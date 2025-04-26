package com.aspodev.web.Controller;

import com.aspodev.web.Exception.UrlException;
import com.aspodev.web.Service.UrlService;
import com.aspodev.web.model.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private final UrlService url;

    @Autowired
    public UrlController(UrlService url) {
        this.url = url;
    }

    @GetMapping
    public ResponseEntity<Url> getText() {
        try {
            String uRl = url.getUrl();
            return ResponseEntity.ok(new Url(uRl));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    public ResponseEntity<Url> postUrl() throws IOException {
        try {
            String uRl = url.getUrl();
            return ResponseEntity.ok(new Url(uRl));
        } catch (IOException | UrlException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}