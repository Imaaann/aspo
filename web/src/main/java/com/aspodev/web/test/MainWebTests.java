package com.aspodev.web.test;

import com.aspodev.web.MainWeb;
import com.aspodev.web.Service.DockerService;
import com.github.dockerjava.api.DockerClient;
import com.aspodev.web.Service.DockerService;
import com.github.dockerjava.api.DockerClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainWebTests {


    @Mock
    private DockerClient dockerClient;

    @InjectMocks
    private DockerService dockerService;

    @Test
    public void test() {
        // Mock any DockerClient interactions if needed
        // Example: Mock inspectContainerCmd if used in other methods
        when(dockerClient.inspectContainerCmd(anyString())).thenReturn(mock(com.github.dockerjava.api.command.InspectContainerCmd.class));

        // Test DockerService behavior
        String result = dockerService.LastCommand();
        assertEquals("Docker@web-Container$NONE", result);
    }


}
