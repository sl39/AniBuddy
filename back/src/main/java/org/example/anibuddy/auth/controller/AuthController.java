package org.example.anibuddy.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.auth.Dto.AuthSignUpRequestDto;
import org.example.anibuddy.auth.service.SignupService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public String signup(@RequestBody AuthSignUpRequestDto authSignUpRequestDto) throws Exception {
        signupService.signup(authSignUpRequestDto);
        return "회원가입 성공!";
    }

    @GetMapping("/autoLogin")
    public void autoLogin(){}






}
