package org.example.anibuddy.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.user.UserDto.UserSignUpRequestDto;
import org.example.anibuddy.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody UserSignUpRequestDto userSignUpRequestDto) throws Exception {
        userService.signup(userSignUpRequestDto);
        return "회원가입 성공!";
    }




}
