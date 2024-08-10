package com.example.front;

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
        public String getPetName () {
            return petName;
        }

        public void setPetName (String petName){
            this.petName = petName;
        }

        public String getPetKind () {
            return petKind;
        }

        public void setPetKind (String petKind){
            this.petKind = petKind;
        }

        public String getPetGender () {
            return petGender;
        }

        public void setPetGender (String petGender) {
            this.petGender = petGender;
        }

        public String getBase64Image () {
        return base64Image;
    }

        public void setBase64Image (String base64Image) {
        this.base64Image = base64Image;
    }

        public Integer getPetAge () {
            return petAge;
        }

        public void setPetAge(Integer petAge) {
            this.petAge = petAge;
        }

        public Integer getPetId () {
        return petId;
    }

        public void setPetId(Integer petId) {this.petId = petId; }
}