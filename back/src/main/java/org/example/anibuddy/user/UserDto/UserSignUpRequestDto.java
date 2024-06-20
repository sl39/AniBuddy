package org.example.anibuddy.user.UserDto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserSignUpRequestDto {
    private String userName;

    private String email;

    private String password;

    private String password2;

    private String userPhone;

    private String userAddress;
}
