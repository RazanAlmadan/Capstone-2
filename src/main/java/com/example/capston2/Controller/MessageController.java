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
    public ResponseEntity<?> addMessage(@RequestBody @Valid Message message){
        messageService.addMessage(message);
        return ResponseEntity.status(200).body(new ApiResponse("Message was added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer id, @RequestBody @Valid Message message){
        messageService.updateMessage(id, message);
        return ResponseEntity.status(200).body(new ApiResponse("Message was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer id){
        messageService.deleteMessage(id);
        return ResponseEntity.status(200).body(new ApiResponse("Message was deleted"));
    }
}