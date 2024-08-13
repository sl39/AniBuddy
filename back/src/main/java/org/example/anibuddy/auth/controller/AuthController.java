package org.example.anibuddy.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.auth.Dto.AuthEmailDto;
import org.example.anibuddy.auth.Dto.AuthNicknameDto;
import org.example.anibuddy.auth.Dto.AuthSignUpRequestDto;
import org.example.anibuddy.auth.service.SignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody AuthSignUpRequestDto authSignUpRequestDto) throws Exception {
        ResponseEntity response = signupService.signup(authSignUpRequestDto);
        return response;
    }

    @PostMapping("/autoLogin")
    public Map<String,String> autoLogin(Map<String,String> map) throws Exception {
        System.out.println(map.toString());
        String message = "토큰이 재발급 되었습니다";
        return Map.of("message", message);
    }

    @PostMapping("/signup/signupAll")
    public ResponseEntity<?> signupAll(@RequestBody List<AuthSignUpRequestDto> authSignUpRequestDto) throws Exception {
        ResponseEntity response = signupService.signupAll(authSignUpRequestDto);
        return response;
    }

    @PostMapping("/signup/check/email")
    public Boolean checkEmail(@RequestBody AuthEmailDto authEmailDto) throws Exception {
        System.out.println(authEmailDto.getEmail());
        return signupService.checkEmail(authEmailDto.getEmail());
    }

    @PostMapping("/signup/check/nickname")
    public Boolean checkNickname(@RequestBody AuthNicknameDto authNicknameDto) throws Exception {
        return signupService.checkNickname(authNicknameDto.getNickname());
    }

}
