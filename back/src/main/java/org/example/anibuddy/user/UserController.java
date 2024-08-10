package org.example.anibuddy.user;


import java.util.List;

import org.example.anibuddy.pet.PetDTO;
import org.example.anibuddy.pet.PetDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    
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
}


//