package org.example.anibuddy.user;

import java.util.List;

import org.example.anibuddy.pet.PetEntity;
import org.example.anibuddy.following.FollowingEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class UserEntity {

//    public String getUserName() {
//		return userName;
//	}
//
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getUserPhone() {
//		return userPhone;
//	}
//
//	public void setUserPhone(String userPhone) {
//		this.userPhone = userPhone;
//	}
//
//	public String getUserAddress() {
//		return userAddress;
//	}
//
//	public void setUserAddress(String userAddress) {
//		this.userAddress = userAddress;
//	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userPhone;

    @Column(nullable = true)
    private String userAddress;
    
	@OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)	
	private List<PetEntity> petEntity;
	
}
