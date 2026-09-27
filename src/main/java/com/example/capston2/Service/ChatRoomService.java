package com.example.capston2.Service;

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

    public Boolean updateChatRoom(Integer id, ChatRoom chatRoom){
        ChatRoom oldChatRoom = chatRoomRepository.findChatRoomById(id);
        if (oldChatRoom == null){
            return false;
        }
        oldChatRoom.setRequestId(chatRoom.getRequestId());
        oldChatRoom.setCreatedAt(chatRoom.getCreatedAt());
        chatRoomRepository.save(oldChatRoom);
        return true;
    }

    public Boolean deleteChatRoom(Integer id){
        ChatRoom oldChatRoom = chatRoomRepository.findChatRoomById(id);
        if (oldChatRoom == null){
            return false;
        }
        chatRoomRepository.delete(oldChatRoom);
        return true;
    }

    public List<Message> printMessages(Integer chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        if (chatRoom == null){
            return null;
        }
        List<Message> messages = messageRepository.findMessageByChatRoomId(chatRoomId);
        return messages;
    }
}