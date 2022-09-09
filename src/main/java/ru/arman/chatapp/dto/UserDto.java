package ru.arman.chatapp.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Component
@Data
@Getter
@Setter
public class UserDto {
    @NotBlank(message = "Username cannot be empty")
    @Size(min=2, max=50)
    private String username;
    @NotBlank(message = "Email can not be empty")
    @Email(message = "Please provide a valid email")
    private String email;
    @NotBlank(message = "Password can not be empty")
    private String password;
    @NotBlank(message = "Password can not be empty")
    private String confirmPassword;
}
