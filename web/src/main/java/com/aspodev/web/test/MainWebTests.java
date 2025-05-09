package com.aspodev.web.test;

import com.aspodev.web.MainWeb;
import com.aspodev.web.Service.DockerService;
import com.github.dockerjava.api.DockerClient;
import com.aspodev.web.Service.DockerService;
import com.github.dockerjava.api.DockerClient;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


@SpringBootTest
public class MainWebTests {


    @Test
    public void test() throws InterruptedException {
        try {
            DockerService dockerService = new DockerService();
            String imageName = "openjdk:17-slim";
            String containerName = "Aspo";
            int port = 8080;

            // Create and run the container
            dockerService.createAndRunContainer(imageName, containerName, port);

            dockerService.execCommand(containerName, "echo Hello World");

            // Check if the container is running
            boolean isRunning = dockerService.isContainerRunning(containerName);
            assertTrue(isRunning);

            // Stop and remove the container
            dockerService.stopAndRemoveContainer(containerName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
