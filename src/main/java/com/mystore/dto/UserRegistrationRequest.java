package com.mystore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequest {

    @NotBlank(message = "Ім'я користувача не може бути порожнім")
    @Size(min = 3, max = 50, message = "Ім'я користувача повинно бути від 3 до 50 символів")
    private String username;

    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Некоректний формат email")
    private String email;

    private String phone;

    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 6, message = "Пароль повинен містити щонайменше 6 символів")
    private String password;
}