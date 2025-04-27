package com.aspodev.web.Controller;

import com.aspodev.web.Service.DockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/docker/containers")
public class DockerController {

    private final DockerService dockerService;

    @Autowired
    public DockerController(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    @GetMapping
    public ResponseEntity<List<String>> listContainers() {
        try {
            return ResponseEntity.ok(dockerService.listContainers());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createContainer(
            @RequestParam String image,
            @RequestParam(required = false ,defaultValue = "openjdk:jdk-17-slim")  String name) {
        try {
            if (image == null || image.isBlank()) {
                return ResponseEntity.badRequest().body("Image name is required");
            }

            String containerId = dockerService.createContainer(image);
            return ResponseEntity.ok("Container created with ID: " + containerId);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error creating container: " + e.getMessage());
        }
    }

    @PostMapping("/{containerId}/start")
    public ResponseEntity<String> startContainer(@PathVariable String containerId) {
        try {
            if (containerId == null || containerId.isBlank()) {
                return ResponseEntity.badRequest().body("Container ID is required");
            }

            dockerService.startContainer(containerId);
            return ResponseEntity.ok("Container started successfully");
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error starting container: " + e.getMessage());
        }
    }

    @PostMapping("/{containerId}/stop")
    public ResponseEntity<String> stopContainer(@PathVariable String containerId) {
        try {
            if (containerId == null || containerId.isBlank()) {
                return ResponseEntity.badRequest().body("Container ID is required");
            }

            dockerService.stopContainer(containerId);
            return ResponseEntity.ok("Container stopped successfully");
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error stopping container: " + e.getMessage());
        }
    }

    @DeleteMapping("/{containerId}")
    public ResponseEntity<String> removeContainer(@PathVariable String containerId) {
        try {
            if (containerId == null || containerId.isBlank()) {
                return ResponseEntity.badRequest().body("Container ID is required");
            }

            dockerService.removeContainer(containerId);
            return ResponseEntity.ok("Container removed successfully");
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error removing container: " + e.getMessage());
        }
    }

    @PostMapping("/{containerId}/clone")
    public ResponseEntity<String> cloneRepository(
            @PathVariable String containerId,
            @RequestParam String url,
            @RequestParam(required = false, defaultValue = "/CloneDir") String path) {
        try {
            if (containerId == null || containerId.isBlank()) {
                return ResponseEntity.badRequest().body("Container ID is required");
            }
            if (url == null || url.isBlank()) {
                return ResponseEntity.badRequest().body("Repository URL is required");
            }

            dockerService.cloneinsideContainer(containerId, url, path);
            return ResponseEntity.ok("Repository cloned successfully into " + path);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error cloning repository: " + e.getMessage());
        }
    }

    @GetMapping("/{containerId}/files")
    public ResponseEntity<?> listFiles(
            @PathVariable String containerId,
            @RequestParam(required = false, defaultValue = "/CloneDir") String path) {
        try {
            if (containerId == null || containerId.isBlank()) {
                return ResponseEntity.badRequest().body("Container ID is required");
            }

            List<String> files = dockerService.getClonedFiles(containerId, path);
            return ResponseEntity.ok(files);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error listing files: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}