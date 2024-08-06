package org.example.anibuddy.auth.Dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthSignUpRequestDto {
    private String userName;

    private String email;

    private String password;

    private String password2;

    private String userAddress;

    private String nickname;
}
