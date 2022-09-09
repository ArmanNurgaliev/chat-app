package ru.arman.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassDto {
    @NotBlank(message = "Username cannot be empty")
    @Size(min=2, max=50)
    private String username;
    @NotBlank(message = "Username can't be empty")
    private String password;
    @NotBlank(message = "Username can't be empty")
    private String passwordNew;
    @NotBlank(message = "Username can't be empty")
    private String passwordVerifyNew;
}
