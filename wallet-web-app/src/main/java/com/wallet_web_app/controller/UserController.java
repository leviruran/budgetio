package com.wallet_web_app.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wallet_web_app.controller.AuthenticationController;
import com.wallet_web_app.dto.ChangePasswordRequest;
import com.wallet_web_app.dto.UpdateUserRequest;
import com.wallet_web_app.dto.UserProfileResponse;
import com.wallet_web_app.dto.UserResponse;
import com.wallet_web_app.entity.ImageModel;
import com.wallet_web_app.entity.Role;
import com.wallet_web_app.entity.User;
import com.wallet_web_app.exceptions.AppException;
import com.wallet_web_app.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = {"/update"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserResponse> updateUser(@RequestPart("user") UpdateUserRequest updateUserRequest,
                                                   @RequestPart("imageFile") MultipartFile[] file, Principal connectedUser) {
        // Log the request data before updating
        logRequestData(updateUserRequest, connectedUser);
        try {
            Set<ImageModel> images = uploadImage(file);
            updateUserRequest.setUserImages(images);
            UserResponse updatedUser = service.updateUser(updateUserRequest, connectedUser);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(null); // or return an appropriate ResponseEntity with an error message
        }
    }

    private void logRequestData(UpdateUserRequest updateUserRequest, Principal connectedUser) {
        // Implement logging logic
        System.out.println("Updating user: " + connectedUser.getName() + " with data: " + updateUserRequest);
    }

	public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();

        for (MultipartFile file: multipartFiles) {
            ImageModel imageModel = new ImageModel(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            imageModels.add(imageModel);
        }

        return imageModels;
    }
    

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all/users")
    public List<UserProfileResponse> getAllUsers() {
        try {
            return service.getAllUsers();
        } catch (Exception e) {
            // Log the error
            logger.error("An error occurred while retrieving all users", e);
            // You may choose to throw or handle the exception further if needed
            // For simplicity, I'm just returning an empty list here
            return Collections.emptyList();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/usersByRoles")
	public List<UserProfileResponse> getAllUsersByRoles(@RequestParam Role role) {
		return service.getAllUsersByRoles(role);
	}

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/user/{id}")
    public void deleteProduct(@PathVariable Long id) {
    	service.deleteUser(id);
    }
    
    @PreAuthorize("hasRole('USER')")
	@GetMapping("/user/{userId}")
    public UserResponse getUserById(@PathVariable Long userId) {
        Optional<User> userOptional = service.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserResponse(user);
        } else {
            // Handle the case where the user with the given ID is not found
            throw new AppException("User with ID " + userId + " not found", HttpStatus.NOT_FOUND);
        }
    }

}
