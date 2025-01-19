package com.wallet_web_app.services;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet_web_app.dto.AuthenticationRequest;
import com.wallet_web_app.dto.AuthenticationResponse;
import com.wallet_web_app.dto.RegisterRequest;
import com.wallet_web_app.dto.UserDto;
import com.wallet_web_app.dto.UserProfileRequest;
import com.wallet_web_app.entity.Role;
import com.wallet_web_app.entity.Token;
import com.wallet_web_app.entity.User;
import com.wallet_web_app.exceptions.AppException;
import com.wallet_web_app.repository.UserRepository;
import com.wallet_web_app.token.TokenRepository;
import com.wallet_web_app.token.TokenType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;


  public AuthenticationResponse register(RegisterRequest request, Role role) {

      Optional<User> optionalUser = getUserByEmail(request.getEmail());
      if (optionalUser.isPresent()) {
    	  throw new AppException("User Email Already exists.", HttpStatus.BAD_REQUEST);
      }
      LocalDate registerDate = LocalDate.now();
      User user = User.builder()
              .firstname(request.getFirstname())
              .lastname(request.getLastname())
              .email(request.getEmail())
              .password(passwordEncoder.encode(request.getPassword()))
              .role(role)
              .joinDate(registerDate)
              .build();

      User savedUser = repository.save(user);
      UserDto userResponse = UserDto.builder()
    	    	.id(savedUser.getId())
    	  		  .firstname(savedUser.getFirstname())
    	  		  .lastname(savedUser.getLastname())
    	  		  .email(savedUser.getEmail())
    	  		  .role(savedUser.getRole())
    	  		  .build();
      String jwtToken = jwtService.generateToken(savedUser);
      String refreshToken = jwtService.generateRefreshToken(savedUser);
      saveUserToken(savedUser, jwtToken);
      return AuthenticationResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .user(userResponse)
              .build();
}


  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );

    LocalDate lastLogin = LocalDate.now();
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    user.setLastLogin(lastLogin);
    UserDto userResponse = UserDto.builder()
    		.id(user.getId())
  		  .firstname(user.getFirstname())
  		  .lastname(user.getLastname())
  		  .email(user.getEmail())
  		  .role(user.getRole())
  		  .build();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .user(userResponse)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

public Optional<User> getUserByEmail(String Email){
	return repository.findByEmail(Email);
}
public Optional<User> getUserById(Long id){
	return repository.findById(id);
}


public void updateUser(UserProfileRequest request, Principal connectedUser) {

    var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    Optional<User> userOptional = getUserById(user.getId());
    if(userOptional.isPresent()) {
        User usert = userOptional.get();
        usert.setFirstname(request.getFirstname());
        usert.setLastname(request.getLastname());
        usert.setUserImages(request.getUserImages());
        repository.save(usert);
    }
}
}
