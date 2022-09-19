package ru.arman.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassDto {
    @NotBlank(message = "Email cannot be empty")
    @Email
    private String email;
    @NotBlank(message = "Username can't be empty")
    private String password;
    @NotBlank(message = "Username can't be empty")
    private String passwordNew;
    @NotBlank(message = "Username can't be empty")
    private String passwordVerifyNew;
}
