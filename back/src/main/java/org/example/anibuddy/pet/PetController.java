package org.example.anibuddy.pet;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pet")
@RequiredArgsConstructor
public class PetController {

	private final PetService petService;
	
	@GetMapping("/list")
	public String petList(Model model) {
		List<PetEntity> petList = this.petService.getList();
		model.addAttribute("petList", petList);
		return "/list";
	}

	// 펫 등록버튼을 누르고 보이는 펫 프로필 추가 화면에서 받은 정보를 DB에 저장.
	// 권한이고 뭐고 일단 아무것도 안함.
	
	@PostMapping("/create")
	public ResponseEntity<ApiResponse> createPetProfile(
			@RequestBody PetCreateDTO petCreateDTO,
			@RequestParam(value = "userId") Integer userId) {
//			@RequestParam(value = "petName") String petName,
//			@RequestParam(value = "petKind") String petKind,
//			@RequestParam(value = "petAge") Integer petAge,
//			@RequestParam(value = "petGender") String petGender,
//            @RequestParam(value = "petNeutering") String petNeutering,
//            @RequestParam(value = "petChipNumber") Long petChipNumber,
//            @RequestParam(value = "petSignificant") String petSignificant,
            
		if (userId == null || userId <= 0) {
			ApiResponse response = new ApiResponse(false, "해당 유저를 찾을 수 없습니다!");
	        return ResponseEntity.badRequest().body(response);
	    } else {
	    	ApiResponse response = new ApiResponse(true, "프로필을 성공적으로 추가했습니다!");
	    	petService.createPetProfile(petCreateDTO, userId); {
	    		return ResponseEntity.ok(response);
//	    			petCreateDTO.getPetName(), petCreateDTO.getPetKind(), petCreateDTO.getPetAge(), petCreateDTO.getPetGender(), petCreateDTO.getPetNeutering(), petCreateDTO.getPetChipNumber(), petCreateDTO.getPetSignificant(), userId
	    	}
	    }
	}		
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<PetEntity>> getPetEntityByUserId(@PathVariable("userId") int userId) {
		List<PetEntity> pet = petService.getPetByUserId(userId);
		return ResponseEntity.ok(pet);
		// userId에 연결된 petEntity(쉽게 말해 펫 프로필) 전부 List형태로 가져오기.
		// 여기서 추가코딩으로 화면에 뿌려줄 수 있음.
	}
	
//	// 펫 프로필 수정하기 버튼 누른 화면에서 받은 정보 수정해서 프로필에 저장. userId는 수정하지 않음.
//	@PostMapping("/modify")
//	public ResponseEntity<String> editPetProfile(
//			@RequestBody PetCreateDTO petCreateDTO, 
//			@RequestParam(value = "petId") Integer petId,
//            @RequestParam(value = "userId") Integer userId) {
////			@RequestParam(value = "petName") String petName,
////			@RequestParam(value = "petKind") String petKind,
////			@RequestParam(value = "petAge") Integer petAge,
////			@RequestParam(value = "petGender") String petGender,
////            @RequestParam(value = "petNeutering") String petNeutering,
////            @RequestParam(value = "petChipNumber") Long petChipNumber,
////            @RequestParam(value = "petSignificant") String petSignificant,
//
//		petService.editPetProfile(petCreateDTO, petId, userId);
////				petId, petCreateDTO.getPetName(), petCreateDTO.getPetKind(), petCreateDTO.getPetAge(), petCreateDTO.getPetGender(), petCreateDTO.getPetNeutering(), petCreateDTO.getPetChipNumber(), petCreateDTO.getPetSignificant(), userId); {
//		return ResponseEntity.ok("프로필을 성공적으로 수정했습니다!");
//		}
	
	// 펫 프로필 수정하기 버튼 누른 화면에서 받은 정보 수정해서 프로필에 저장. userId는 수정하지 않음. + 24.07.31. userId 제거
	@PostMapping("/modify")
	public ResponseEntity<String> editPetProfile(
			@RequestBody PetCreateDTO petCreateDTO, 
			@RequestParam(value = "petId") Integer petId) {
//			@RequestParam(value = "petName") String petName,
//			@RequestParam(value = "petKind") String petKind,
//			@RequestParam(value = "petAge") Integer petAge,
//			@RequestParam(value = "petGender") String petGender,
//            @RequestParam(value = "petNeutering") String petNeutering,
//            @RequestParam(value = "petChipNumber") Long petChipNumber,
//            @RequestParam(value = "petSignificant") String petSignificant,

		petService.editPetProfile(petCreateDTO, petId);
//				petId, petCreateDTO.getPetName(), petCreateDTO.getPetKind(), petCreateDTO.getPetAge(), petCreateDTO.getPetGender(), petCreateDTO.getPetNeutering(), petCreateDTO.getPetChipNumber(), petCreateDTO.getPetSignificant(), userId); {
		return ResponseEntity.ok("프로필을 성공적으로 수정했습니다!");
		}
	
	// 프로필 삭제. 원하는 건 profile 화면에서 등록되어있는 profile 클릭하면 해당id 받아오면서 정보 뿌려주는 화면인데,
	// 거기서 삭제 버튼 누르면 차피 petId 받아와져 있으니까 그거 이용해서 삭제
	@PostMapping("/delete/{petId}")
	public ResponseEntity<String> deletPetProfile(@PathVariable("petId") Integer petId) {
		PetEntity petEntity = petService.getPetById(petId);
		if(petEntity != null ) {
			petService.deletePetProfile(petEntity);
			return ResponseEntity.ok("프로필이 성공적으로 삭제되었습니다!");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 프로필을 찾을 수 없습니다!");
		}
	}
}




//try {
//System.out.println("Creating pet profile with userId: " + userId);
//petService.createPetProfile(petName, petKind, petAge, petGender, petNeutering, petChipNumber, petSignificant, userId);
//return ResponseEntity.ok("Pet profile created successfully.");
//} catch (Exception e) {
//e.printStackTrace();
//return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating pet profile: " + e.getMessage());
//}
//}