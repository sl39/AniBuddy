package org.example.anibuddy.user;

import jakarta.persistence.*;
import jakarta.servlet.http.PushBuilder;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    private String refreshToken; // 리프레시 토큰



    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updatedRefreshToken){
        this.refreshToken = updatedRefreshToken;
    }


}
