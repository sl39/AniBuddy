package org.example.anibuddy.store;

import jakarta.persistence.*;
import lombok.*;
import org.example.anibuddy.Review.ReviewEntity;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = true)
    private String storeName;

    @Column(nullable = true)
    private String storeAddress;

    private String info;

    private String phoneNumber;

    @Column(nullable = true)
    private Long mapx;

    @Column(nullable = true)
    private Long mapy;

    @OneToMany(mappedBy = "Store", cascade = CascadeType.REMOVE)
    private List<ReviewEntity> reviewList;
}
