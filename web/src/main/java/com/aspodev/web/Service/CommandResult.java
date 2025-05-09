package com.aspodev.web.Service;

public class CommandResult {
    private String command;
    private String stdout;
    private String stderr;
    private int exitCode;

    CommandResult(String command, String stdout, String stderr, int exitCode) {
        this.command = command;
        this.stdout = stdout;
        this.stderr = stderr;
        this.exitCode = exitCode;
    }

    public CommandResult() {
        this.command = "";
        this.stdout = "";
        this.stderr = "";
        this.exitCode = 0;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getCommand() {
        return command;
    }

    public String getStderr() {
        return stderr;
    }

    public String getStdout() {
        return stdout;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }
}
