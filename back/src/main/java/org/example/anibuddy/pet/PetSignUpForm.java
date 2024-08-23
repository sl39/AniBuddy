package org.example.anibuddy.pet;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetSignUpForm {
	
	@NotEmpty(message="반려동물의 이름은 필수 항목입니다.")
	private String petName;
	
	@NotEmpty(message="반려동물의 성별은 필수 항목입니다.")
	private String petGender;
	
	@NotEmpty(message="반려동물의 나이는 필수 항목입니다.")
	private String petAge;
	
	@NotEmpty(message="반려동물의 중성화 여부는 필수 항목입니다.")
	private String petNeutering;
	
	@NotEmpty(message="반려동물의 품종은 필수 항목입니다.")
	private String petKind;
}