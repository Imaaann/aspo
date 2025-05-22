package com.aspo.Web.Controller; 

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aspo.Web.Model.Command;
import com.aspo.Web.Model.CommandResult;
import com.aspo.Web.Service.CommandService;
import com.aspo.Web.Util.JsonReading; 


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:5500"})
public class CommandController {

    private final CommandService commandService;

    public CommandController(CommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping("/execute")
    public ResponseEntity<CommandResult> executeCommand(@RequestBody Command command) { 
        String inputFilePath = "resources/data/command.json"; 
        String outputFilePath = "resources/data/output.json"; 
        CommandResult result = new CommandResult();

        try {
            JsonReading.writeCommand(inputFilePath, command); 
            commandService.executeCommandFromFile(inputFilePath, outputFilePath); 
            result = JsonReading.readOutput(outputFilePath); 
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/result")
    public ResponseEntity<CommandResult> getResult() {
        CommandResult result = commandService.getCommandResult();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}