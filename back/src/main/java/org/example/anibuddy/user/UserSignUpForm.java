package org.example.anibuddy.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserSignUpForm {

	@NotEmpty(message="이메일은 필수 항목입니다.")
	private String email;
	
	@NotEmpty(message="비밀번호는 필수 항목입니다.")
	private String password;
	
	@NotEmpty(message="주소는 필수 항목입니다.")
	private String address;
	
	@NotEmpty(message="이름은 필수 항목입니다.")
	private String username;
	
	@NotEmpty(message="핸드폰 번호는 필수 항목입니다.")
	private String userphone;
}







