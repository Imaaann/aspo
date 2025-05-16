package com.aspodev.web.Controller;

import com.aspodev.web.Model.Command;
import com.aspodev.web.Model.CommandResult;
import com.aspodev.web.Service.CommandService;
import com.aspodev.web.Service.JsonRead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/command")
public class CommandController {

    @Autowired
    private CommandService commandService;

    @PostMapping("/execute")
    public ResponseEntity<CommandResult> executeCommand(@RequestBody Command command) {
        try {
            // Validate the command
            if (command == null || command.getCommand() == null || command.getCommand().trim().isEmpty()) {
                return new ResponseEntity<>(
                    new CommandResult(command, "", "Invalid or empty command", -1),
                    HttpStatus.BAD_REQUEST
                );
            }

            CommandResult result = commandService.executeCommand(command);

            // Check if there was an error in execution
            if (!result.getStderr().isEmpty() || result.getExitCode() != 0) {
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Return successful result
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (InterruptedException e) {
            // Handle interruption during command execution
            Thread.currentThread().interrupt(); // Restore interrupted status
            return new ResponseEntity<>(
                new CommandResult(command, "", "Command execution interrupted: " + e.getMessage(), -1),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (Exception e) {
            // Handle any other unexpected errors
            return new ResponseEntity<>(
                new CommandResult(command, "", "Unexpected error: " + e.getMessage(), -1),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/test")
    public String test() {
        return JsonRead.readOut("C:\\Users\\Imad\\Desktop\\New folder (2)\\aspo\\web\\src\\resources\\data\\OutPut.json");
    }
}