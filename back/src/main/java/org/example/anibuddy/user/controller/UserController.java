package org.example.anibuddy.user.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.example.anibuddy.global.CustomUserDetails;
import org.example.anibuddy.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public Map<String,String> getUserList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Integer id = userDetails.getUserId();
        System.out.println(id.toString());
        String userName = userService.getUser(email);
        Map<String,String> map = new HashMap<>();
        map.put("userName",userName);
        return map;
    }
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
