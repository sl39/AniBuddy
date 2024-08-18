package org.example.anibuddy.owner;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.user.UserEntity;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class OwnerEntity {
    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    @OneToMany(mappedBy = "ownerEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<StoreEntity> stores;

    @OneToOne
    @JsonBackReference
    private UserEntity userEntity;
}