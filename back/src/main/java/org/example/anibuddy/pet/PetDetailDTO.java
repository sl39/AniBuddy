package org.example.anibuddy.pet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetDetailDTO {
	private String petName, petKind, petGender, petNeutering, petSignificant, base64Image;
    private Integer petAge, petId;
    private long petChipNumber;
    
    public PetDetailDTO() {
    }
    
    public PetDetailDTO(String petName, String petKind, String petGender, String petNeutering, String petSignificant, String base64Image,
    					Integer petAge, Integer petId, long petChipNumber) {
        this.petName = petName;
        this.petKind = petKind;
        this.petGender = petGender;
        this.petNeutering = petNeutering;
        this.petSignificant = petSignificant;
        this.base64Image = base64Image;
        this.petAge = petAge;
        this.petId = petId;
        this.petChipNumber = petChipNumber;

    }
}