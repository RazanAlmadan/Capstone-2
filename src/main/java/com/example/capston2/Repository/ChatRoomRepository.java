package com.example.capston2.Repository;

import com.example.capston2.Model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    ChatRoom findChatRoomById(Integer id);
}
