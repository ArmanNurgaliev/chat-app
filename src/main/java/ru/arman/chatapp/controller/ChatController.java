package ru.arman.chatapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.arman.chatapp.dto.MessageDto;
import ru.arman.chatapp.model.ChatMessage;
import ru.arman.chatapp.model.ChatRoom;
import ru.arman.chatapp.model.User;
import ru.arman.chatapp.service.ChatMessageService;
import ru.arman.chatapp.service.ChatRoomService;
import ru.arman.chatapp.service.UserService;

import java.util.List;

@RestController
@Slf4j
public class ChatController {
    private ChatRoomService chatRoomService;
    private ChatMessageService chatMessageService;
    private UserService userService;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(ChatRoomService chatRoomService, ChatMessageService chatMessageService, UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.chatRoomService = chatRoomService;
        this.chatMessageService = chatMessageService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/private-messages/{recipientName}")
    public void privateMessage(@DestinationVariable String recipientName,
                               MessageDto messageDto) throws InterruptedException {
        log.info("MessageDto in privateMessage: " + messageDto);
     //    Thread.sleep(1000);
        chatMessageService.createMessage(messageDto);

        messagingTemplate.convertAndSend("/queue/messages/" + recipientName, messageDto);
    }

    @GetMapping("/get-current-username")
    public String getCurrentUser(@AuthenticationPrincipal User user) {
        return user.getUsername();
    }

    @GetMapping("/get-recipient")
    public User getRecipient(@RequestParam String recipientName) {
        return userService.getUserByFullName(recipientName);
    }

    @GetMapping("/get-chats")
    public List<ChatRoom> getRooms(@AuthenticationPrincipal User user) {
        List<ChatRoom> chatRooms = chatRoomService.getChatRooms(user);
        log.info(String.valueOf(chatRooms));
        return chatRooms;
    }

    @GetMapping("/get-chat")
    public List<ChatMessage> getRoom(@AuthenticationPrincipal User user,
                            @RequestParam String recipientName) {
       // return chatRoomService.getChatRoom(user, userService.getUserByUsername(recipientName));
        return chatMessageService.getChatMessages(user, userService.getUserByFullName(recipientName));
    }

    @GetMapping("/get-users")
    public List<User> getFriends(@AuthenticationPrincipal User user) {
        return userService.getAllUsers(user);
    }

    @GetMapping("/read-messages")
    public void readMessages(@AuthenticationPrincipal User user,
                             @RequestParam String recipientName) {
        chatMessageService.readMessages(user.getFullName(), recipientName);
    }

}
