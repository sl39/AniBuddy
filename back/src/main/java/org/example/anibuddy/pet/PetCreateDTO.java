package org.example.anibuddy.pet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetCreateDTO {
	private String petName, petKind, petNeutering, petGender, petSignificant, petCategory, base64Image;
	private Integer petAge;
	private long petChipNumber;
}