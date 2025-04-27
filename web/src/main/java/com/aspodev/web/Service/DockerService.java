package com.aspodev.web.Service;
import com.amihaiemil.docker.Docker;
import com.amihaiemil.docker.Container;
import com.amihaiemil.docker.Containers;
import com.amihaiemil.docker.UnixDocker;
import com.aspodev.web.Controller.GitClone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DockerService {

    private final Docker docker;
    private static final Logger logger = LoggerFactory.getLogger(DockerService.class);
    private final String file = "files.json";

    public Docker getDocker() {
        return docker;
    }
    public String getContainerId(){
        return docker.containers().get("").containerId();
    }
    public String getFile() {
        return file;
    }

    public DockerService() {
        this.docker = new UnixDocker(new File("/Clone/docker.sock"));
    }

    public List<String> listContainers() {
        Containers containers = this.docker.containers();
        return StreamSupport.stream(containers.spliterator(), false)
                .map(Container::containerId)
                .collect(Collectors.toList());
    }

    public String createContainer(String image) throws IOException {
        Container container = this.docker.containers()
                .create(image);
        return container.containerId();
    }

    public void startContainer(String containerId) throws IOException {
        this.docker.containers()
                .get(containerId)
                .start();
    }

    public void stopContainer(String containerId) throws IOException {
        this.docker.containers()
                .get(containerId)
                .stop();
    }

    public void removeContainer(String containerId) throws IOException {
        this.docker.containers()
                .get(containerId)
                .remove();
    }

    public void cloneinsideContainer(String containerId,String url,String path) throws IOException{
        if (url == null | path == null) {
            throw new IOException("Error url is null");
        }
        Container container = docker.containers().get(containerId);
        ProcessBuilder processBuilder = new ProcessBuilder("mkdir",path,"&&","cd",path);
        Process process = processBuilder.start();
        GitClone.cloneRepository(url,"./CloneDir");
    }

    public List<String> getfilesName(String containerId,String path) throws IOException, InterruptedException {
        Container container = docker.containers().get(containerId);
        ProcessBuilder processBuilder = new ProcessBuilder("cd",path,"&&","ls");

        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Repository cloned successfully to: " + path);
        } else {
            System.err.println("Failed to clone repository. Exit code: " + exitCode);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> files = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            files.add(line);
        }
        return files;
    }

    public List<String> getClonedFiles(String containerId,String path) throws IOException, InterruptedException {
        return getfilesName(containerId,path);
    }
}