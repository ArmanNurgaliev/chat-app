package ru.arman.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.arman.chatapp.model.ChatMessage;
import ru.arman.chatapp.model.User;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findAllBySenderAndRecipient(User sender, User recipient);
}
