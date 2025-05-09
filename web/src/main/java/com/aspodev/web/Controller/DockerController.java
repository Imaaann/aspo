package com.aspodev.web.Controller;

import com.aspodev.web.Exception.DockerException;
import com.aspodev.web.Service.DockerService;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @PostMapping
    public ResponseEntity<CreateContainerResponse> createContainer(@RequestParam String imageName, @RequestParam String containerName, @RequestParam int port) throws InterruptedException {
        return ResponseEntity.ok(dockerService.createAndRunContainer(imageName, containerName, port));
    }

    @PostMapping("/{containerId}/clone")
    public ResponseEntity<String> cloneRepository(@PathVariable String containerId, @RequestParam String url, @RequestParam String path) throws IOException, InterruptedException {
        dockerService.cloneinsideContainer(containerId, url, path);
        return ResponseEntity.ok(dockerService.LastCommand());
    }

    @PostMapping("/{containerId}/exec")
    public ResponseEntity<String> executeCommand(@PathVariable String containerId, @RequestBody String[] command) throws InterruptedException, DockerException {
        dockerService.execCommand(containerId, command);
        return ResponseEntity.ok(dockerService.LastCommand());
    }

    //TODO: Control the commands inside the container


}