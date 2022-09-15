package ru.arman.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.arman.chatapp.model.ChatRoom;
import ru.arman.chatapp.model.User;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findBySenderAndRecipient(User sender, User recipient);

    List<ChatRoom> findAllByRecipient(User recipient);
}
