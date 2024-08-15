package com.example.front;

public class PetDetailDTO {
    private String petName, petKind, petNeutering, petGender, petSignificant, petCategory, base64Image;
    private Integer petAge;
    private Integer petId;
    private long petChipNumber;

    public PetDetailDTO() {
    }

    public PetDetailDTO(String petName, String petKind, String petNeutering, String petGender, String petSignificant, String petCategory, String base64Image, Integer petAge, Integer petId, long petChipNumber) {
        this.petName = petName;
        this.petKind = petKind;
        this.petGender = petGender;
        this.petNeutering = petNeutering;
        this.petSignificant = petSignificant;
        this.petCategory = petCategory;
        this.base64Image = base64Image;
        this.petAge = petAge;
        this.petId = petId;
        this.petChipNumber = petChipNumber;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetKind() {
        return petKind;
    }

    public void setPetKind(String petKind) {
        this.petKind = petKind;
    }

    public String getPetNeutering() {
        return petNeutering;
    }

    public void setPetNeutering(String petNeutering) {
        this.petNeutering = petNeutering;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }

    public String getPetSignificant() {
        return petSignificant;
    }

    public void setPetSignificant(String petSignificant) {
        this.petSignificant = petSignificant;
    }

    public String getPetCategory() { return petCategory; }

    public void setPetCategory(String petCategory) {
        this.petCategory = petCategory;
    }

    public String getBase64Image() { return base64Image; }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public Integer getPetAge() {
        return petAge;
    }

    public void setPetAge(Integer petAge) {
        this.petAge = petAge;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public long getPetChipNumber() {
        return petChipNumber;
    }

    public void setPetChipNumber(long petChipNumber) {
        this.petChipNumber = petChipNumber;
    }
}