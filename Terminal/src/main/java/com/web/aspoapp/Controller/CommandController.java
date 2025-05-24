package com.web.aspoapp.Controller; 

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.web.aspoapp.Model.Command;
import com.web.aspoapp.Model.CommandResult;
import com.web.aspoapp.Service.CommandService;
import com.web.aspoapp.Util.CommandExecutor;
import com.web.aspoapp.Util.Json;
import org.springframework.web.bind.annotation.GetMapping;


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
        if (command == null || command.getCommand() == null || command.getCommand().trim().isEmpty()) {
            return new ResponseEntity<>(new CommandResult(new Command(""), "Commande vide.","error", 1), HttpStatus.BAD_REQUEST);
        }

        try {
            // Directly execute the command via the service
            CommandResult result = commandService.executeCommand(command);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CommandResult(new Command(""),"", "Erreur interne du serveur: " + e.getMessage(), 1), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //current working dir
    @GetMapping("/cwd")
    public ResponseEntity<String> getCurrentWorkingDirectory() {
        return new ResponseEntity<>(commandService.getCurrentWorkingDirectoryPath(), HttpStatus.OK);
    }

    @GetMapping("/result")
    public ResponseEntity<CommandResult> getResult() {
        CommandExecutor result = commandService.getCommandResult();
        return new ResponseEntity<>(result.getCommandResult(), HttpStatus.OK);
    }

    @GetMapping("/Latest")
    public String getMethodName() throws IOException{
        return Json.readCommand("C:\\Users\\Imad\\Desktop\\New folder (2)\\Latest\\aspoapp\\src\\main\\resources\\command.json").getCommand();
    }
    
}