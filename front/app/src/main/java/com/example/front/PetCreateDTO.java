package com.example.front;

public class PetCreateDTO {
    private String petName, petKind, petNeutering, petGender, petSignificant, petCategory, base64Image, petAge;
    private long petChipNumber;

    public PetCreateDTO() {
    }

    // 매개변수가 있는 생성자
    public PetCreateDTO(String petName, String petKind, String petNeutering, String petGender, String petSignificant, String petCategory, String base64Image, String petAge, long petChipNumber) {
        this.petName = petName;
        this.petKind = petKind;
        this.petGender = petGender;
        this.petNeutering = petNeutering;
        this.petSignificant = petSignificant;
        this.petCategory = petCategory;
        this.base64Image = base64Image;
        this.petAge = petAge;
        this.petChipNumber = petChipNumber;
    }

    public String getPetName() { return petName; }

    public void setPetName(String petName) { this.petName = petName; }

    public String getPetKind() { return petKind; }

    public void setPetKind(String petKind) { this.petKind = petKind; }

    public String getPetNeutering() { return petNeutering; }

    public void setPetNeutering(String petNeutering) { this.petNeutering = petNeutering; }

    public String getPetGender() { return petGender; }

    public void setPetGender(String petGender) { this.petGender = petGender; }

    public String getPetSignificant() { return petSignificant; }

    public void setPetSignificant(String petSignificant) { this.petSignificant = petSignificant; }

    public String getPetCategory() { return petCategory; }

    public void setPetCategory(String petCategory) { this.petCategory = petCategory; }
    public String getBase64Image() { return base64Image; }

    public void setBase64Image(String base64Image) { this.base64Image = base64Image; }

    public String getPetAge() { return petAge; }

    public void setPetAge(String petAge) { this.petAge = petAge; }

    public long getPetChipNumber() { return petChipNumber; }

    public void setPetChipNumber(long petChipNumber) { this.petChipNumber = petChipNumber; }
}