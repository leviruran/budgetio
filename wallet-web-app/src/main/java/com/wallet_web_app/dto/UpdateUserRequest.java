package com.wallet_web_app.dto;

import java.util.Set;

import com.wallet_web_app.entity.ImageModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateUserRequest {

    private String firstname;
    private String lastname;

    @NotEmpty(message = "Email is required.")
    @Email(message = "Valid email is required.")
    private String email;

    private int age;
    private String gender;
    private Set<ImageModel> userImages;
}

