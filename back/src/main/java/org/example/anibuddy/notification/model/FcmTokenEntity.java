package org.example.anibuddy.notification.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.user.UserEntity;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "fcm_token_entity")
public class FcmTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String role;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private OwnerEntity owner;

    @Column(nullable = false)
    private String fcmToken;

    @Builder
    public FcmTokenEntity(
            String role,
            UserEntity userEntity,
            OwnerEntity ownerEntity,
            String fcmToken
    ){
        this.role = role;
        this.user = userEntity;
        this.owner = ownerEntity;
        this.fcmToken = fcmToken;
    }
}
