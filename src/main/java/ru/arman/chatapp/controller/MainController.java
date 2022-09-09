package ru.arman.chatapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.arman.chatapp.model.ChatRoom;
import ru.arman.chatapp.model.User;
import ru.arman.chatapp.service.ChatRoomService;
import ru.arman.chatapp.service.UserService;

import java.util.List;

@Controller
@Slf4j
public class MainController {
    private final UserService userService;
    private final ChatRoomService chatRoomService;

    @Autowired
    public MainController(UserService userService, ChatRoomService chatRoomService) {
        this.userService = userService;
        this.chatRoomService = chatRoomService;
    }

    @GetMapping("")
    public String index(@AuthenticationPrincipal User user, Model model) {
        if (user == null)
            return "redirect:/login";

        List<ChatRoom> chatRooms = chatRoomService.getChatRooms(user);

        model.addAttribute("chatRooms", chatRooms);

        return "index";
    }

}
