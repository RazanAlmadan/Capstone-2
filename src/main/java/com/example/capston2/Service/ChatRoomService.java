package com.example.capston2.Service;

import com.example.capston2.Api.ApiException;
import com.example.capston2.Model.ChatRoom;
import com.example.capston2.Model.Message;
import com.example.capston2.Repository.ChatRoomRepository;
import com.example.capston2.Repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public List<ChatRoom> getAllChatRooms(){
        return chatRoomRepository.findAll();
    }

    public void addChatRoom(ChatRoom chatRoom){
        chatRoomRepository.save(chatRoom);
    }

    public void updateChatRoom(Integer id, ChatRoom chatRoom){
        ChatRoom oldChatRoom = chatRoomRepository.findChatRoomById(id);
        if (oldChatRoom == null){
            throw new ApiException("chat room not found");
        }
        oldChatRoom.setRequestId(chatRoom.getRequestId());
        oldChatRoom.setCreatedAt(chatRoom.getCreatedAt());
        chatRoomRepository.save(oldChatRoom);
    }

    public void deleteChatRoom(Integer id){
        ChatRoom oldChatRoom = chatRoomRepository.findChatRoomById(id);
        if (oldChatRoom == null){
            throw new ApiException("chat room not found");
        }
        chatRoomRepository.delete(oldChatRoom);
    }

    public List<Message> printMessages(Integer chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        if (chatRoom == null){
            throw new ApiException("chat room not found");
        }
        List<Message> messages = messageRepository.findMessageByChatRoomId(chatRoomId);
        if (messages.isEmpty()){
            throw new ApiException("No messages was found");
        }
        return messages;
    }
}