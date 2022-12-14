package ru.arman.chatapp.service;

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
        ChatRoom chatRoom = chatRoomRepository.findBySenderNameAndRecipientName(recipient.getFullName(), sender.getFullName());
        if (chatRoom == null ) {
            ChatRoom chatRoomSender =
                    ChatRoom.builder()
                            .senderName(sender.getFullName())
                            .recipientName(recipient.getFullName())
                            .chatMessages(new ArrayList<>())
                            .lastUpdate(new Date())
                            .build();

            ChatRoom chatRoomRecipient =
                    ChatRoom.builder()
                            .senderName(recipient.getFullName())
                            .recipientName(sender.getFullName())
                            .chatMessages(new ArrayList<>())
                            .lastUpdate(new Date())
                            .build();

            chatRoomRepository.save(chatRoomRecipient);
            return chatRoomRepository.save(chatRoomSender);
        }
        return chatRoom;
    }

    public List<ChatRoom> getChatRooms(User user) {
        return chatRoomRepository.findAllByRecipientName(user.getFullName()).stream()
                .sorted(Comparator.comparing(ChatRoom::getLastUpdate).reversed())
                .collect(Collectors.toList());
    }
}
