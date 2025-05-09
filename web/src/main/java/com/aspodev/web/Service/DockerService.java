package com.aspodev.web.Service;

import com.aspodev.web.Exception.DockerException;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DockerClientBuilder;
import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
public class DockerService {

    private final DockerClient dockerClient;
    private static String terminal;
    private static CommandResult commandResult;
    private final String file = "files.json";

    @Value("${docker.command.prefix:Docker@web-Container$}")
    private String commandPrefix;

    public DockerService() {
        this(createDefaultDockerClient());
    }

    // Constructor for dependency injection (e.g., testing)
    public DockerService(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
        this.commandResult = new CommandResult();
    }


    private static DockerClient createDefaultDockerClient() {
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(URI.create("npipe:////./pipe/docker_engine")) // Default for Docker Desktop on Windows
                // .dockerHost("tcp://localhost:2375") // Use if TCP is enabled
                .build();
        return DockerClientBuilder.getInstance()
                .withDockerHttpClient(httpClient)
                .build();
    }

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

    public void execCommand(String containerId,String... command) throws InterruptedException, DockerException {
        ExecCreateCmdResponse Command = dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd(command)
                .exec();
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        dockerClient.execStartCmd(Command.getId())// execute la commande
                .exec(new ExecStartResultCallback(stdout, stderr))
                .awaitCompletion(30, TimeUnit.SECONDS);
        terminal = Arrays.toString(command) +"\n"+ stdout.toString()+stderr.toString();
    }

    public String LastCommand(){
        return terminal!=null ?
                """
                        {
                    "command": "%s",
                    "stdout": "%s",
                    "stderr": "%s",
                    "exitCode": %d
                }
                """.formatted(commandResult.getCommand(), commandResult.getStdout(), commandResult.getStderr(), commandResult.getExitCode())


        : """
                
                {
                    "command": "",
                    "stdout": "",
                    "stderr": "",
                    "exitCode": 0
                }
                """;
    }

    public String getFile() {
        return file;
    }

    public boolean isContainerRunning(String containerName) {
        try {
            InspectContainerResponse container = dockerClient.inspectContainerCmd(containerName).exec();
            return Boolean.TRUE.equals(container.getState().getRunning());
        } catch (Exception e) {
            return false; // Container not found or not running
        }
    }
}
