package org.example.anibuddy.user;

import jakarta.persistence.*;
import lombok.*;
import org.example.anibuddy.Review.ReviewEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.example.anibuddy.user.Role;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="user_entity")
@Builder
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeId;

    @Column(nullable = false)
    private String userName;

    private String nickname; // 닉네임
    private String socialId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userPhone;

    @Column(nullable = true)
    private String userAddress;

    private String refreshToken; // 리프레시 토큰

    private String imageUrl; // 프로필 이미지

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @OneToMany(mappedBy = "UserEntity", cascade = CascadeType.REMOVE)
    private List<ReviewEntity> reviewEntity;

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


}
