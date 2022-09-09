package ru.arman.chatapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.arman.chatapp.dto.ChangePassDto;
import ru.arman.chatapp.dto.UserDto;
import ru.arman.chatapp.model.User;
import ru.arman.chatapp.service.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class RegistrationController {
    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDto", new UserDto());

        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@Valid UserDto userDto, BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()){
            return "registration";
        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            model.addAttribute("confirmation", "Passwords are not equals");
            return "registration";
        }

        Map<String, String> errors = new HashMap<>();
        if (!userService.registerUser(userDto, errors)) {
            if (errors.containsKey("usernameExist"))
                model.addAttribute("usernameExist",
                        String.format("An account with username %s already exists", errors.get("usernameExist")));
            if (errors.containsKey("emailExist"))
                model.addAttribute("emailExist",
                        String.format("An account with email %s already exists", errors.get("emailExist")));
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/change-pass")
    public String changePassword(Model model) {
        model.addAttribute("changePassDto", new ChangePassDto());
        return "change-password";
    }

    @PostMapping("/change-pass")
    public String changePassword(@Valid ChangePassDto changePassDto,
                                 BindingResult bindingResult,
                                 Model model) {

        if (bindingResult.hasErrors()){
            return "change-password";
        }

        User user = userService.getUserByUsername(changePassDto.getUsername());
        if (user == null) {
            model.addAttribute("userNotFound", "No user with such username");
            return "change-password";
        }

        if (!userService.checkPasswords(changePassDto, user)) {
            model.addAttribute("pass", "Wrong password");
            return "change-password";
        }

        if (!changePassDto.getPasswordNew().equals(changePassDto.getPasswordVerifyNew())) {
            model.addAttribute("confirmation", "Passwords are not equals");
            return "change-password";
        }

        userService.changePassword(user, changePassDto);

        return "redirect:/login";
    }

}
