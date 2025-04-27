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

    private final UrlService url;
    private final DockerService files;

    @Autowired
    public UrlController(UrlService url,DockerService files) {
        this.url = url;
        this.files =files;
    }

    @GetMapping("/url")
    public ResponseEntity<Url> getText() {
        try {
            String uRl = url.getUrl();
            return ResponseEntity.ok(new Url("https://www.github.com"));
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

    //post files's name

    @PostMapping
    public ResponseEntity<List<String>> postFiles() throws IOException{
        try {
            List<String> listOfiles = files.getClonedFiles(files.getContainerId(),files.getFile());
            return ResponseEntity.ok(listOfiles);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}