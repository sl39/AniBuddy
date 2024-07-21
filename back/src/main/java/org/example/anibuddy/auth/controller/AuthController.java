package org.example.anibuddy.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.auth.Dto.AuthSignUpRequestDto;
import org.example.anibuddy.auth.service.SignupService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public Map signup(@RequestBody AuthSignUpRequestDto authSignUpRequestDto) throws Exception {
        signupService.signup(authSignUpRequestDto);
        Map<String, String> map = new HashMap<>();
        map.put("status", "success");
        return map;
    }

    @PostMapping("/autoLogin")
    public Map<String,String> autoLogin(Map<String,String> map) throws Exception {
        System.out.println(map.toString());
        String message = "토큰이 재발급 되었습니다";
        return Map.of("message", message);
    }






}
