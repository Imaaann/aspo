package com.aspodev.web.Service;

import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import org.springframework.stereotype.Service;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DockerClientBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class DockerService {

    private final DockerClient dockerClient;
    private static String terminal;
    private final String file = "files.json";

    public DockerService() {
        this.dockerClient = DockerClientBuilder.getInstance().build();
    }
//
//    public List<String> listContainers() {
//        Containers containers = this.docker.containers();
//        return StreamSupport.stream(containers.spliterator(), false)
//                .map(Container::containerId)
//                .collect(Collectors.toList());
//    }
//
//    public String createContainer(String image) throws IOException {
//        Container container = this.docker.containers().create(image);
//        return container.containerId();
//    }
//
//    public void startContainer(String containerId) throws IOException {
//        this.docker.containers().get(containerId).start();
//    }
//
//    public void stopContainer(String containerId) throws IOException {
//        this.docker.containers().get(containerId).stop();
//    }
//
//    public void removeContainer(String containerId) throws IOException {
//        this.docker.containers().get(containerId).remove();
//    }

    public CreateContainerResponse createAndRunContainer(String imageName, String containerName, int port) throws InterruptedException {
        System.out.println("Creating...");
        dockerClient.pullImageCmd(imageName).start().awaitCompletion();// installing the image

        CreateContainerResponse container = dockerClient.createContainerCmd(imageName)
                .withName(containerName)
                .withPortBindings(com.github.dockerjava.api.model.PortBinding.parse(port + ":" + port))
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();
        return container;
    }

    public void stopAndRemoveContainer(String containerName) {
        dockerClient.stopContainerCmd(containerName).exec();//stop it before removing
        dockerClient.removeContainerCmd(containerName).exec();// Remove container
        System.out.println("Container stopped and removed successfully");
    }

    /**
     * Execute a command in a running container and return the output
     *
     * @param containerId ID or name of the container
     * @param command The command and its arguments to execute
     * @return The output of the command
     * @throws IOException If there's an error executing the command
     * @throws InterruptedException If the execution is interrupted
     */




    public void cloneinsideContainer(String containerId, String url, String path) throws IOException, InterruptedException {
        if (url == null || url.isBlank() || path == null || path.isBlank()) {
            throw new IOException("URL and path must not be null or empty");
        }
        // ensure the container is running
        InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(containerId).exec();
        if (Boolean.FALSE.equals(containerResponse.getState().getRunning())) {
            throw new IOException("Container is not running");
        }

        ExecCreateCmdResponse command = dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd("git", "clone", url, path)
                .exec();

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        dockerClient.execStartCmd(command.getId())// execute la commande
                .exec(new ExecStartResultCallback(stdout, stderr))
                .awaitCompletion(30, TimeUnit.SECONDS);

        terminal = "git clone "+ path + url + stdout.toString()+stderr.toString();
    }

    public static void execCommand(String containerId,String... command){
        ExecCreateCmdResponse command = dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd(command)
                .exec();
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        dockerClient.execStartCmd(command.getId())// execute la commande
                .exec(new ExecStartResultCallback(stdout, stderr))
                .awaitCompletion(30, TimeUnit.SECONDS);
        terminal = command + stdout.toString()+stderr.toString();
    }

    public String LastCommand(){
        return terminal!=null ? "Docker@web-Container$"+terminal : "NONE";
    }

//    public List<String> getClonedFiles(String containerId, String path) throws IOException, InterruptedException {
//        Container container = docker.containers().get(containerId);
//
//        Process process = execInContainer(containerId, new String[]{"ls", path});
//        int exitCode = process.waitFor();
//        if (exitCode != 0) {
//            throw new IOException("Failed to list files. Exit code: " + exitCode);
//        }
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        List<String> files = new ArrayList<>();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            files.add(line);
//        }
//        return files;
//    }
//
//    private Process execInContainer(String containerId, String[] command) throws IOException {
//        // I used Docker exec to run commands inside the container
//        String[] dockerCommand = Stream.concat(Stream.of("docker", "exec", containerId), Stream.of(command))
//                .toArray(String[]::new);
//        ProcessBuilder processBuilder = new ProcessBuilder(dockerCommand);
//        return processBuilder.start();
//    }

    public String getFile() {
        return file;
    }
}