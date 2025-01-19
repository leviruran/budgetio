package com.wallet_web_app.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import com.wallet_web_app.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private Long id;
	private String firstname;
	private String lastname;
	private String email;
	private Role role;
}
