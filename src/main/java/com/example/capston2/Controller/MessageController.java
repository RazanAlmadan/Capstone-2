package com.example.capston2.Controller;

import com.example.capston2.Api.ApiResponse;
import com.example.capston2.Model.Message;
import com.example.capston2.Service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllMessages(){
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMessage(@RequestBody @Valid Message message, Errors errors){
        if (errors.hasErrors()){
            String messageText = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(messageText);
        }
        messageService.addMessage(message);
        return ResponseEntity.status(200).body(new ApiResponse("Message was added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer id, @RequestBody @Valid Message message, Errors errors){
        if (errors.hasErrors()){
            String messageText = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(messageText);
        }
        Boolean results = messageService.updateMessage(id, message);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Message was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer id){
        Boolean results = messageService.deleteMessage(id);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Message was deleted"));
    }
}