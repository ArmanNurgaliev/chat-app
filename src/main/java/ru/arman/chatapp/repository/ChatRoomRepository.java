package ru.arman.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.arman.chatapp.model.ChatRoom;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findBySenderNameAndRecipientName(String sender, String recipient);

    List<ChatRoom> findAllByRecipientName(String recipient);
}
