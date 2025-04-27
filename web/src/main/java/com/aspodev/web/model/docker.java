package com.aspodev.web.model;

import com.amihaiemil.docker.Docker;

import java.util.List;

public class docker {
    private Docker container;

    docker(Docker container) {
        this.container = container;
    }

    public void setFilesname(Docker container) {
        this.container = container;
    }

    public Docker getFilesname() {
        return container;
    }
}
