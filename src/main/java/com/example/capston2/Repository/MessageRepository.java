package com.example.capston2.Repository;

import com.example.capston2.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    Message findMessageById(Integer id);
    List<Message> findMessageByChatRoomId(Integer chatRoomId);
}
