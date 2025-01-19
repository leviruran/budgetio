package com.wallet_web_app.dto;

import java.util.Set;

import com.wallet_web_app.entity.ImageModel;
import com.wallet_web_app.entity.User;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private int age;
    private String gender;
    private Set<ImageModel> userImages;

    // Constructor to initialize UserResponse from a User entity
    public UserResponse(User user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.userImages = user.getUserImages();
    }
}

