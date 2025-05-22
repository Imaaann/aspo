package com.aspo.Web.Util;

import com.aspo.Web.Model.Command;
import com.aspo.Web.Model.CommandResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException; 

public class JsonReading {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Command readCommand(String filePath) throws IOException {
        return objectMapper.readValue(new File(filePath), Command.class);
    }

    public static void writeCommand(String filePath, Command command) throws IOException {
        objectMapper.writeValue(new File(filePath), command);
    }

    public static CommandResult readOutput(String filePath) throws IOException {
        return objectMapper.readValue(new File(filePath), CommandResult.class);
    }

    public static void writeOutput(String filePath, CommandResult result) throws IOException {
        objectMapper.writeValue(new File(filePath), result);
    }
}