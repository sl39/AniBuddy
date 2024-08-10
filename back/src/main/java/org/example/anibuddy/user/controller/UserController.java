package org.example.anibuddy.user.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.global.CustomUserDetails;
import org.example.anibuddy.pet.PetDTO;
import org.example.anibuddy.pet.PetDetailDTO;
import org.example.anibuddy.user.UserCreateDto;
import org.example.anibuddy.user.UserDTO;
import org.example.anibuddy.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(
//			@RequestParam(value = "userName") String userName,
//			@RequestParam(value = "email") String email,
//			@RequestParam(value = "password") String password,
//            @RequestParam(value = "userPhone") String userPhone,
//            @RequestParam(value = "userAddress") String userAddress
            @RequestBody UserCreateDto userCreateDto) {

        userService.createUser(userCreateDto.getUserName(), userCreateDto.getEmail(), userCreateDto.getPassword(), userCreateDto.getUserPhone(), userCreateDto.getUserAddress()); {
            return ResponseEntity.ok("회원가입 성공!");
        }
    }


    @GetMapping("/profile")
    public ResponseEntity<List<PetDTO>> getPetByUserId(@RequestParam(value = "userId") Integer userId) {
        List<PetDTO> pets = userService.getPetByUserId(userId);
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/profile/detail")
    public ResponseEntity<PetDetailDTO> getPetDetailById(@RequestParam(value = "petId") Integer petId) {
        PetDetailDTO petDetail = userService.getPetDetailDTO(petId);
        return ResponseEntity.ok(petDetail);
    }

    @GetMapping("/lists")
    public ResponseEntity<UserDTO> getUserInfoById(@RequestParam(value = "id") Integer id) {
        UserDTO userDTO = userService.getUserDTO(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("")
    public Map<String,String> getUserList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Integer id = userDetails.getUserId();
        System.out.println(email);
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
