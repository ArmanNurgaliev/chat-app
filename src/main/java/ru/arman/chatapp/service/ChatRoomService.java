package ru.arman.chatapp.service;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.arman.chatapp.model.ChatRoom;
import ru.arman.chatapp.model.User;
import ru.arman.chatapp.repository.ChatMessageRepository;
import ru.arman.chatapp.repository.ChatRoomRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatRoomService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomService(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom getChatRoom(User sender, User recipient) {
        ChatRoom chatRoom = chatRoomRepository.findBySenderAndRecipient(sender, recipient);
        if (chatRoom == null ) {
            ChatRoom chatRoomSender =
                    ChatRoom.builder()
                            .sender(sender)
                            .recipient(recipient)
                            .lastUpdate(new Date())
                            .build();

            ChatRoom chatRoomRecipient =
                    ChatRoom.builder()
                            .sender(recipient)
                            .recipient(sender)
                            .lastUpdate(new Date())
                            .build();

            chatRoomRepository.save(chatRoomRecipient);
            return chatRoomRepository.save(chatRoomSender);
        }
        return chatRoom;
    }

    public List<ChatRoom> getChatRooms(User user) {
        return chatRoomRepository.findAllBySender(user).stream()
                .sorted(Comparator.comparing(ChatRoom::getLastUpdate))
                .collect(Collectors.toList());
    }
}
