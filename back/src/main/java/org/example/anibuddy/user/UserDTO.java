package org.example.anibuddy.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	private String userName, email, userAddress;
	private Integer userId;
	
	public UserDTO() {
	}
	
	public UserDTO(String userName, String email, String userAddress, Integer userId) {
		this.userName = userName;
		this.email = email;
		this.userAddress = userAddress;
		this.userId = userId;
	}
}