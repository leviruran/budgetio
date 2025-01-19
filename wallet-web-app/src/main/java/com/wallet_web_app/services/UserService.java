package com.wallet_web_app.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wallet_web_app.dto.ChangePasswordRequest;
import com.wallet_web_app.dto.UpdateUserRequest;
import com.wallet_web_app.dto.UserProfileResponse;
import com.wallet_web_app.dto.UserResponse;
import com.wallet_web_app.entity.Role;
import com.wallet_web_app.entity.User;
import com.wallet_web_app.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;


    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

    private List<UserProfileResponse> mapUserToUserProfile(List<User> users) {
    	List<UserProfileResponse> userProfiles = new ArrayList<>();
    	for(User user: users) {
    		UserProfileResponse userProfile = new UserProfileResponse();
    		userProfile.setId(user.getId());
    		userProfile.setFirstname(user.getFirstname());
    		userProfile.setLastname(user.getLastname());
    		userProfile.setEmail(user.getEmail());
    		userProfile.setUserImages(user.getUserImages());
    		userProfiles.add(userProfile);
    	}
    	return userProfiles;
    }


    public List<UserProfileResponse> getAllUsers() {
        try {
            List<User> users = repository.findAll();
            return mapUserToUserProfile(users);
        } catch (Exception e) {
            // Handle the exception, log it, or return an appropriate response
            // For example:
            e.printStackTrace(); // Logging the exception
            return Collections.emptyList(); // Returning an empty list
        }
    }

    public List<UserProfileResponse> getAllUsersByRoles(Role role) {
    	List<User> users = repository.findAllByRole(role);
    	return mapUserToUserProfile(users);
    }


	public void deleteUser(Long userId) {
		repository.deleteById(userId);
	}
	
	public UserResponse updateUser(UpdateUserRequest updateUserRequest, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (user != null) {
            user.setFirstname(updateUserRequest.getFirstname());
            user.setLastname(updateUserRequest.getLastname());
            user.setEmail(updateUserRequest.getEmail());
            user.setAge(updateUserRequest.getAge());
            user.setGender(updateUserRequest.getGender());
            user.setUserImages(updateUserRequest.getUserImages());
            User updatedUser = repository.save(user);
            return new UserResponse(updatedUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }

	public Optional<User> findById(Long userId) {
		return repository.findById(userId);
	}
}

