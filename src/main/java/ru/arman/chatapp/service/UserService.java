package ru.arman.chatapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.arman.chatapp.dto.ChangePassDto;
import ru.arman.chatapp.dto.UserDto;
import ru.arman.chatapp.model.Role;
import ru.arman.chatapp.model.User;
import ru.arman.chatapp.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(email);
        if (user != null) return user;
        else
            throw new UsernameNotFoundException(String.format("Username with email %s not found", email));
    }

    public boolean registerUser(UserDto userDto, Map<String, String> exist) {
        boolean emailExists = userRepository.findByEmail(userDto.getEmail()) != null;
        boolean userExists = userRepository.findByUsername(userDto.getUsername()).isPresent();
        if (emailExists || userExists) {
            if (userExists)
                exist.put("usernameExist", userDto.getUsername());
            if (emailExists)
                exist.put("emailExist", userDto.getEmail());
            return false;
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(userDto.getConfirmPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        userRepository.save(user);

        return true;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> getAllUsers(User user) {
        return  userRepository.findAll().stream()
                .filter(user1 -> !user1.getEmail().equals(user.getEmail()))
                .collect(Collectors.toList());
    }

    public void changePassword(User user, ChangePassDto changePassDto) {
        user.setPassword(passwordEncoder.encode(changePassDto.getPasswordNew()));

        userRepository.save(user);
    }

    public boolean checkPasswords(ChangePassDto changePassDto, User user) {
        return passwordEncoder.matches(changePassDto.getPassword(), user.getPassword());
    }
}
