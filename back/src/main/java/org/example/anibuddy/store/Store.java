package org.example.anibuddy.store;

import jakarta.persistence.*;
import lombok.*;
import org.example.anibuddy.Review.ReviewEntity;
import org.hibernate.annotations.CollectionId;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeId;

    @Column(nullable = true)
    private String storeName;

    @Column(nullable = true)
    private Long mapx;

    @Column(nullable = true)
    private Long mapy;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ReviewEntity> reviewList;
}
