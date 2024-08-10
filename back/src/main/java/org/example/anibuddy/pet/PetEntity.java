package org.example.anibuddy.pet;

import org.example.anibuddy.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class PetEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer petId;
	
	@Column(nullable = false)
	private String petCategory;

    @Column(nullable = false)
    private String petName;

    @Column(nullable = false)
    private String petKind;

	@Column(nullable = false)
    private Integer petAge;

    @Column(nullable = false)
    private String petGender;

    @Column(nullable = false)
    private String petNeutering;
    
    @Column(nullable = false)
    private Long petChipNumber;
    
    @Column(nullable = false)
    private String petSignificant;
    
    @Column(nullable = false)
    private String base64Image;
    
	@ManyToOne
	@JoinColumn(name = "userId")
	private UserEntity userEntity;
    
}