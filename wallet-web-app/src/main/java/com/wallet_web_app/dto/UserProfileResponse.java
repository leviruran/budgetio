package com.wallet_web_app.dto;

import java.util.Set;

import com.wallet_web_app.entity.ImageModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
	private Long id;
    private Set<ImageModel> userImages;
	private String firstname;
	private String lastname;
	private String email;
}
