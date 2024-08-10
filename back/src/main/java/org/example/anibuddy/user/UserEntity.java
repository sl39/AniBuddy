package org.example.anibuddy.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.anibuddy.Review.entity.ReviewEntity;
import org.example.anibuddy.pet.PetEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Setter
@Builder
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String nickname; // 닉네임
    private String socialId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String userPhone;

    @Column(nullable = true)
    private String userAddress;

    private String refreshToken; // 리프레시 토큰

    private String imageUrl; // 프로필 이미지

    @JsonBackReference
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ReviewEntity> reviewEntityList;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    public void authorizeUser() {
        this.role = Role.USER;
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updatedRefreshToken){
        this.refreshToken = updatedRefreshToken;
    }



	@OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
	private List<PetEntity> petEntity;

}
