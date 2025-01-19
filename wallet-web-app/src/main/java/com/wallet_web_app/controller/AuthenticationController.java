package com.wallet_web_app.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wallet_web_app.controller.AuthenticationController;
import com.wallet_web_app.dto.AuthenticationRequest;
import com.wallet_web_app.dto.AuthenticationResponse;
import com.wallet_web_app.dto.RegisterRequest;
import com.wallet_web_app.dto.UserProfileResponse;
import com.wallet_web_app.entity.ImageModel;
import com.wallet_web_app.entity.Role;
import com.wallet_web_app.entity.User;
import com.wallet_web_app.services.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService service;

	@PostMapping("/register/user")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request,
			@RequestParam Role role) {
		if (role == Role.ADMIN) {
			  return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
	      }else {
	    	  return ResponseEntity.ok(service.register(request, role));
	      }
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

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(service.authenticate(request));
	}

	@PostMapping("/refresh-token")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		service.refreshToken(request, response);
	}
	
    @GetMapping("/user/{userId}")
    public UserProfileResponse getAllUsers(@PathVariable Long userId ) {
        try {
            java.util.Optional<User> Optionaluser = service.getUserById(userId);
            if(Optionaluser.isPresent()) {
            	User user = Optionaluser.get();
                UserProfileResponse userToReturn = new UserProfileResponse();
                userToReturn.setId(user.getId());
                userToReturn.setFirstname(user.getFirstname());
                userToReturn.setLastname(user.getLastname());
                userToReturn.setEmail(user.getEmail());
                userToReturn.setUserImages(user.getUserImages());
                return userToReturn;
            }else {
                return (UserProfileResponse) Collections.emptyList();
            }
        } catch (Exception e) {
            logger.error("An error occurred while retrieving user", e);
            return (UserProfileResponse) Collections.emptyList();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
}
