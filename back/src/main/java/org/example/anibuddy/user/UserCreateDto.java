package org.example.anibuddy.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDto {
	private String userName;
	private String email;
	private String password;
	private String userPhone;
	private String userAddress;
}