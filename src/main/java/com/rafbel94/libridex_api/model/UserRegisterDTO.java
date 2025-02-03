package com.rafbel94.libridex_api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterDTO {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    String email;
    String password;
    String repeatPassword;
}
