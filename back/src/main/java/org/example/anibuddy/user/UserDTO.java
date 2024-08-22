package org.example.anibuddy.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	private String nickname, email, imageUrl;
//	private Integer userId;
	
	public UserDTO() {
	}
	
	public UserDTO(String nickname, String email, String imageUrl) {
		this.nickname = nickname;
		this.email = email;
		this.imageUrl = imageUrl;
//		this.userId = userId;
	}
}