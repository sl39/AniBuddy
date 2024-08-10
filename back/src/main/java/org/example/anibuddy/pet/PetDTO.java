package org.example.anibuddy.pet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetDTO {
	private String petName, petKind, petGender, base64Image;
    private Integer petAge, petId;
    
    public PetDTO() {
    }
	
    public PetDTO(String petName, String petKind, String petGender, String base64Image, Integer petAge, Integer petId) {
        this.petName = petName;
        this.petKind = petKind;
        this.petGender = petGender;
        this.base64Image = base64Image;
        this.petAge = petAge;
        this.petId = petId;    	
    }
}
