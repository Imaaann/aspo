package com.web.aspoapp.Model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class Command {

    @NotBlank(message = "Command cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_\\-. /]+$", message = "Command contains invalid characters")
    private String command;

    public Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {this.command = command;}

    @Override
    public String toString(){
        return command;
    }
}

