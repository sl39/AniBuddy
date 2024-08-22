package org.example.anibuddy.user;

import org.example.anibuddy.global.CustomUserDetails;
import org.example.anibuddy.pet.PetDTO;
import org.example.anibuddy.pet.PetDetailDTO;
import org.example.anibuddy.pet.PetEntity;
import org.example.anibuddy.pet.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PetRepository petRepository;

	public UserDTO getUserDTO(Integer id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getUserId();
		UserEntity userEntity = userRepository.findById(userId)
		.orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다!"));

		return convertToUserDTO(userEntity);
	}

	private UserDTO convertToUserDTO(UserEntity user) {
		UserDTO userDTO = new UserDTO(user.getUserName(), user.getEmail(), user.getUserAddress(), user.getId());
		return userDTO;
	}

	public List<PetDTO> getPetByUserId(Integer id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getUserId();
		List<PetEntity> petEntity = petRepository.findByUserEntityId(userId);
		return petEntity.stream().map(this::convertToDTO).collect(Collectors.toList());
	    }

	public PetDetailDTO getPetDetailDTO(Integer PetId) {
		PetEntity petEntity = petRepository.findById(PetId)
		.orElseThrow(() -> new IllegalArgumentException("해당 프로필이 없습니다!"));

		return convertToDetailDTO(petEntity);
	}

	private PetDTO convertToDTO(PetEntity pet) {
	    return new PetDTO(pet.getPetName(), pet.getPetKind(), pet.getPetGender(), pet.getBase64Image(), pet.getPetAge(), pet.getPetId());
	}

    private PetDetailDTO convertToDetailDTO(PetEntity pet) {
    	PetDetailDTO petDetailDTO = new PetDetailDTO(pet.getPetName(), pet.getPetKind(), pet.getPetGender(), pet.getPetNeutering(), pet.getPetSignificant(), pet.getBase64Image(), pet.getPetAge(), pet.getPetId(), pet.getPetChipNumber());
    	return petDetailDTO;
    }
//    	PetDetailDTO dto = new PetDetailDTO();
//        dto.setPetId(pet.getPetId());
//        dto.setPetName(pet.getPetName());
//        dto.setPetKind(pet.getPetKind());
//        dto.setPetAge(pet.getPetAge());
//        dto.setPetNeutering(pet.getPetNeutering());
//        dto.setPetChipNumber(pet.getPetChipNumber());
//        dto.setPetGender(pet.getPetGender());
//        dto.setPetSignificant(pet.getPetSignificant());
//
//        return dto;

	public UserEntity getUser(Integer Id) {
		Optional<UserEntity> userEntity = this.userRepository.findById(Id);
		if(userEntity.isPresent()) {
			return userEntity.get();
		} else {
			throw new RuntimeException("해당 유저가 존재하지 않습니다!");
		}
	}

	public void createUser(String userName, String email, String password, String userPhone, String userAddress) {
		UserEntity user = new UserEntity();
		user.setEmail(email);
		user.setPassword(password);
		user.setUserAddress(userAddress);
		user.setUserName(userName);
		user.setUserPhone(userPhone);
		this.userRepository.save(user);
	}

	public void editUser(UserEntity userEntity, String userName, String email, String password, String userPhone, String userAddress) {
		userEntity.setEmail(email);
		userEntity.setPassword(password);
		userEntity.setUserAddress(userAddress);
		userEntity.setUserName(userName);
		userEntity.setUserPhone(userPhone);
		this.userRepository.save(userEntity);
	}

	public Optional<UserEntity> getUserById(Integer id) {
		return userRepository.findById(id);
	}

    public String getUser(String email) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email not found")));
        return user.get().getUserName();
    }
    public Optional<UserEntity> getUserByNickname(String nickname) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByNickname(nickname).orElseThrow(() -> new UsernameNotFoundException("email not found")));
        return user;
    }


}