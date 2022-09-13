package ru.arman.chatapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.arman.chatapp.dto.MessageDto;
import ru.arman.chatapp.model.ChatMessage;
import ru.arman.chatapp.model.ChatRoom;
import ru.arman.chatapp.model.MessageStatus;
import ru.arman.chatapp.model.User;
import ru.arman.chatapp.repository.ChatMessageRepository;
import ru.arman.chatapp.repository.ChatRoomRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository, UserService userService, ChatRoomService chatRoomService) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userService = userService;
        this.chatRoomService = chatRoomService;
    }

    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public ChatMessage createMessage(MessageDto messageDto) {
        User recipient = userService.getUserByUsername(messageDto.getRecipientName());
        User sender = userService.getUserByUsername(messageDto.getSenderName());
        ChatRoom chatRoom = chatRoomService.getChatRoom(sender, recipient);

        ChatMessage chatMessage = ChatMessage.builder()
                .senderName(messageDto.getSenderName())
                .recipientName(messageDto.getRecipientName())
                .content(messageDto.getContent())
                .chatRoom(chatRoom)
                .timestamp(messageDto.getTimestamp())
                .status(MessageStatus.DELIVERED)
                .build();

        chatRoom.setLastUpdate(messageDto.getTimestamp());
        chatRoomRepository.save(chatRoom);

        return chatMessageRepository.save(chatMessage);
    }

    public void readMessages(String sender, String recipient) {
        List<ChatMessage> messages = chatMessageRepository.findAllBySenderNameAndRecipientName(recipient, sender);
        for (ChatMessage m: messages) {
            if (m.getStatus() == MessageStatus.DELIVERED) {
                m.setStatus(MessageStatus.RECEIVED);
                chatMessageRepository.save(m);
            }
        }
    }

    public List<ChatMessage> getChatMessages(User sender, User recipient) {
        return Stream.concat(chatMessageRepository.findAllBySenderNameAndRecipientName(sender.getUsername(), recipient.getUsername()).stream(),
                        chatMessageRepository.findAllBySenderNameAndRecipientName(recipient.getUsername(), sender.getUsername()).stream())
                .sorted(Comparator.comparing(ChatMessage::getTimestamp))
                .collect(Collectors.toList());
    }
}
