package com.example.capston2.Controller;

import com.example.capston2.Api.ApiResponse;
import com.example.capston2.Model.ChatRoom;
import com.example.capston2.Model.Message;
import com.example.capston2.Service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllChatRooms(){
        return ResponseEntity.status(200).body(chatRoomService.getAllChatRooms());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addChatRoom(@RequestBody @Valid ChatRoom chatRoom, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        chatRoomService.addChatRoom(chatRoom);
        return ResponseEntity.status(200).body(new ApiResponse("ChatRoom was added"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateChatRoom(@PathVariable Integer id, @RequestBody @Valid ChatRoom chatRoom, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        Boolean results = chatRoomService.updateChatRoom(id, chatRoom);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("ChatRoom was updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteChatRoom(@PathVariable Integer id){
        Boolean results = chatRoomService.deleteChatRoom(id);
        if (!results){
            return ResponseEntity.status(400).body(new ApiResponse("ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("ChatRoom was deleted"));
    }

    @GetMapping("/get/messages/{chatRoomId}")
    public ResponseEntity<?> printMessages(@PathVariable Integer chatRoomId){
        List<Message> messages = chatRoomService.printMessages(chatRoomId);
        if (messages.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No messages was found"));
        }
        if (chatRoomService.printMessages(chatRoomId) == null){
            return  ResponseEntity.status(400).body(new ApiResponse("chatRoom Was not found"));
        }
        return ResponseEntity.status(200).body(messages);
    }
}