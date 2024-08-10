package org.example.anibuddy.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.auth.Dto.AuthSignUpRequestDto;
import org.example.anibuddy.auth.service.SignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/signup")
@RequiredArgsConstructor
public class AuthController {

    private final SignupService signupService;

    @PostMapping("")
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

    @PostMapping("/signupAll")
    public ResponseEntity<?> signupAll(@RequestBody List<AuthSignUpRequestDto> authSignUpRequestDto) throws Exception {
        ResponseEntity response = signupService.signupAll(authSignUpRequestDto);
        return response;
    }

    @PostMapping("/check/email")
    public Boolean checkEmail(@RequestBody String email) throws Exception {
        return signupService.checkEmail(email);
    }

    @PostMapping("check/nickname")
    public Boolean checkNickname(@RequestBody String nickname) throws Exception {
        return signupService.checkNickname(nickname);
    }

}
