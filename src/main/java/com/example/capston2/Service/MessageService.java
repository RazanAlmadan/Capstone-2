package com.example.capston2.Service;

import com.example.capston2.Api.ApiException;
import com.example.capston2.Model.Message;
import com.example.capston2.Repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public void addMessage(Message message){
        messageRepository.save(message);
    }

    public void updateMessage(Integer id, Message message){
        Message oldMessage = messageRepository.findMessageById(id);
        if (oldMessage == null){
            throw new ApiException("message not found");
        }
        oldMessage.setChatRoomId(message.getChatRoomId());
        oldMessage.setSenderId(message.getSenderId());
        oldMessage.setReceiverId(message.getReceiverId());
        oldMessage.setContent(message.getContent());
        oldMessage.setAttachmentUrl(message.getAttachmentUrl());
        oldMessage.setTimeStamp(message.getTimeStamp());
        messageRepository.save(oldMessage);
    }

    public void deleteMessage(Integer id){
        Message oldMessage = messageRepository.findMessageById(id);
        if (oldMessage == null){
           throw new ApiException("message not found");
        }
        messageRepository.delete(oldMessage);
    }
}