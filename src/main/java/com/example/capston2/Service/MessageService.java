package com.example.capston2.Service;

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

    public Boolean updateMessage(Integer id, Message message){
        Message oldMessage = messageRepository.findMessageById(id);
        if (oldMessage == null){
            return false;
        }
        oldMessage.setChatRoomId(message.getChatRoomId());
        oldMessage.setSenderId(message.getSenderId());
        oldMessage.setReceiverId(message.getReceiverId());
        oldMessage.setContent(message.getContent());
        oldMessage.setAttachmentUrl(message.getAttachmentUrl());
        oldMessage.setTimeStamp(message.getTimeStamp());
        messageRepository.save(oldMessage);
        return true;
    }

    public Boolean deleteMessage(Integer id){
        Message oldMessage = messageRepository.findMessageById(id);
        if (oldMessage == null){
            return false;
        }
        messageRepository.delete(oldMessage);
        return true;
    }
}