package com.aspodev.web.Service;

import com.amihaiemil.docker.Container;
import com.amihaiemil.docker.Containers;
import com.amihaiemil.docker.Docker;
import com.amihaiemil.docker.UnixDocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class DockerService {

    private final Docker docker;
    private static final Logger logger = LoggerFactory.getLogger(DockerService.class);
    private final String file = "files.json";

    public DockerService() {
        // Use standard Docker socket
        this.docker = new UnixDocker(new File("/var/run/docker.sock"));
    }

    public List<String> listContainers() {
        Containers containers = this.docker.containers();
        return StreamSupport.stream(containers.spliterator(), false)
                .map(Container::containerId)
                .collect(Collectors.toList());
    }

    public String createContainer(String image) throws IOException {
        Container container = this.docker.containers().create(image);
        return container.containerId();
    }

    public void startContainer(String containerId) throws IOException {
        this.docker.containers().get(containerId).start();
    }

    public void stopContainer(String containerId) throws IOException {
        this.docker.containers().get(containerId).stop();
    }

    public void removeContainer(String containerId) throws IOException {
        this.docker.containers().get(containerId).remove();
    }

    public void cloneinsideContainer(String containerId, String url, String path) throws IOException {
        if (url == null || url.isBlank() || path == null || path.isBlank()) {
            throw new IOException("URL and path must not be null or empty");
        }

        Container container = docker.containers().get(containerId);
        // Ensure git is installed in the container
        execInContainer(containerId, new String[]{"apt-get", "update"});
        execInContainer(containerId, new String[]{"apt-get", "install", "-y", "git"});
        // Create directory and clone repository
        execInContainer(containerId, new String[]{"mkdir", "-p", path});
        execInContainer(containerId, new String[]{"git", "clone", url, path});
    }

    public List<String> getClonedFiles(String containerId, String path) throws IOException, InterruptedException {
        Container container = docker.containers().get(containerId);

        Process process = execInContainer(containerId, new String[]{"ls", path});
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("Failed to list files. Exit code: " + exitCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> files = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            files.add(line);
        }
        return files;
    }

    private Process execInContainer(String containerId, String[] command) throws IOException {
        // I used Docker exec to run commands inside the container
        String[] dockerCommand = Stream.concat(Stream.of("docker", "exec", containerId), Stream.of(command))
                .toArray(String[]::new);
        ProcessBuilder processBuilder = new ProcessBuilder(dockerCommand);
        return processBuilder.start();
    }

    public String getFile() {
        return file;
    }
}