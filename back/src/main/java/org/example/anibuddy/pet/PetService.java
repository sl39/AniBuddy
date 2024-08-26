package org.example.anibuddy.pet;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.example.anibuddy.global.CustomUserDetails;
import org.example.anibuddy.user.UserEntity;
import org.example.anibuddy.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class PetService {
	//서비스 = Repository 이용.

	private final PetRepository petRepository;
	
	//그럼 얘는 왜 ? --> profile 수정할 때 예외처리를 위해서.
	private final UserRepository userRepository;
	
	
	public List<PetEntity> getList(){
		return this.petRepository.findAll();
	}
	
	
	//DataNotFoundException과 IllegalArgumentException의 차이점
	//DataNotFoundException - petId는 있는데 해당하는 데이터가 없다.
	//IllegalArgumentException - petId가 없다.
	public PetEntity getPetById(Integer petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필이 존재하지 않습니다!"));
    }
	
	//Service에 DB 접근 처리(생성).
	public void createPetProfile(@RequestBody PetCreateDTO petCreateDTO, @RequestParam(value = "userId") Integer id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getUserId();
		
//		String petName, String petKind, Integer petAge, String petGender, String petNeutering, Long petChipNumber, String petSignificant, Integer userId) {
		UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));		
		PetEntity pet = new PetEntity();
		pet.setPetName(petCreateDTO.getPetName());
		pet.setPetKind(petCreateDTO.getPetKind());
		pet.setPetAge(petCreateDTO.getPetAge());
		pet.setPetGender(petCreateDTO.getPetGender());
		pet.setPetNeutering(petCreateDTO.getPetNeutering());
		pet.setPetChipNumber(petCreateDTO.getPetChipNumber());
		pet.setPetSignificant(petCreateDTO.getPetSignificant());
		pet.setPetCategory(petCreateDTO.getPetCategory());
		pet.setBase64Image(petCreateDTO.getBase64Image());
		pet.setUserEntity(userEntity);
		this.petRepository.save(pet);
		}
	
	//Service에 DB 접근 처리(수정).
//	public void editPetProfile(@RequestBody PetCreateDTO petCreateDTO, Integer petId, Integer userId) {		
//		PetEntity pet = petRepository.findById(petId)	
//		.orElseThrow(() -> new IllegalArgumentException("수정할 프로필이 없습니다!"));
//		// userId는 있는데 프로필이 없음 
//		
//		// userId도 없음(근데 이럴 수 있나?)
//		//UserEntity user = userRepository.findById(userId)
//	    //        .orElseThrow(() -> new IllegalArgumentException("해당 유저정보가 없습니다!"));
//		
//		pet.setPetName(petCreateDTO.getPetName());
//        pet.setPetKind(petCreateDTO.getPetKind());
//        pet.setPetAge(petCreateDTO.getPetAge());
//        pet.setPetGender(petCreateDTO.getPetGender());
//        pet.setPetNeutering(petCreateDTO.getPetNeutering());
//        pet.setPetChipNumber(petCreateDTO.getPetChipNumber());
//        pet.setPetSignificant(petCreateDTO.getPetSignificant());
//        //pet.setUserEntity(user);
//
//		this.petRepository.save(pet);
//	}
	
	public void editPetProfile(@RequestBody PetCreateDTO petCreateDTO, Integer petId) {		
		PetEntity pet = petRepository.findById(petId)	
		.orElseThrow(() -> new IllegalArgumentException("수정할 프로필이 없습니다!"));
		// userId는 있는데 프로필이 없음 
		
		// userId도 없음(근데 이럴 수 있나?)
		//UserEntity user = userRepository.findById(userId)
	    //        .orElseThrow(() -> new IllegalArgumentException("해당 유저정보가 없습니다!"));
		
		pet.setPetName(petCreateDTO.getPetName());
        pet.setPetKind(petCreateDTO.getPetKind());
        pet.setPetAge(petCreateDTO.getPetAge());
        pet.setPetGender(petCreateDTO.getPetGender());
        pet.setPetNeutering(petCreateDTO.getPetNeutering());
        pet.setPetChipNumber(petCreateDTO.getPetChipNumber());
        pet.setPetSignificant(petCreateDTO.getPetSignificant());
        pet.setPetCategory(petCreateDTO.getPetCategory());
        pet.setBase64Image(petCreateDTO.getBase64Image());
        //pet.setUserEntity(user);

		this.petRepository.save(pet);
	}
	
	//Service에 DB 접근 처리(삭제).
	public void deletePetProfile(PetEntity petEntity) {
		this.petRepository.delete(petEntity);
	}
	
	//해당 userId에 속한 모든 PetEntity List형태로 가져옴
	public List<PetEntity> getPetByUserId(Integer userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer id = userDetails.getUserId();
		return petRepository.findByUserEntityId(id);
	}
}
/*


























	public PetEntity createPetId(@RequestBody PetCreateDTO petCreateDTO, @RequestParam(value = "userId") Integer userId) {
		UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));		
		int count = petRepository.countByMainCategory(petCreateDTO.getMainCategory());
		String petId = generatePetId(petCreateDTO.getMainCategory(), count + 1);
		PetEntity pet = new PetEntity();
		pet.setPetId(petId);
		pet.setPetName(petCreateDTO.getPetName());
		pet.setPetKind(petCreateDTO.getPetKind());
		pet.setPetAge(petCreateDTO.getPetAge());
		pet.setPetGender(petCreateDTO.getPetGender());
		pet.setPetNeutering(petCreateDTO.getPetNeutering());
		pet.setPetChipNumber(petCreateDTO.getPetChipNumber());
		pet.setPetSignificant(petCreateDTO.getPetSignificant());
		pet.setUserEntity(userEntity);
		this.petRepository.save(pet);
	}
	
	private String generatePetId(String mainCategory, int count) {
		switch (mainCategory) {
		case "강아지":
			return "DOG_" + count;
		case "고양이":
			return "CAT_" + count;
		default:
			return "P_" + count;
		}
	}

*/	