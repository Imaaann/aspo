package com.aspodev.web.Controller;

import com.aspodev.web.Model.Command;
import com.aspodev.web.Model.CommandResult;
import com.aspodev.web.Service.CommandService;
import com.aspodev.web.Service.JsonReading;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:8080")
public class CommandController {

    private final CommandService commandService;

    public CommandController(CommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping("/execute")
    public ResponseEntity<CommandResult> executeCommand(@RequestBody Command command) throws InterruptedException {
        System.out.println("Received command: " + command.getCommand());

        commandService.executeCommand(command);
        CommandResult result = commandService.getOutput();


        if (result.getExitCode() == 0) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR); // Or HttpStatus.BAD_REQUEST
        }
    }

    @GetMapping("/pwd")
    public ResponseEntity<File> getCurrentWorkingDirectory() {
         return new ResponseEntity<>(CommandService.getCWD(), HttpStatus.OK);
    }
}