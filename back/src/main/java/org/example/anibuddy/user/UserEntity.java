package org.example.anibuddy.user;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
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
}
