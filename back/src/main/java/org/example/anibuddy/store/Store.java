package org.example.anibuddy.store;

import jakarta.persistence.*;
import lombok.*;
import org.example.anibuddy.Review.ReviewEntity;
import org.example.anibuddy.store.storeMongo.StoreMongoEntity;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints={
                @UniqueConstraint(
                        name = "storeUnique",
                        columnNames = {"store_name", "mapx", "mapy"}
                )
        }
)
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeId;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private Integer mapx;

    @Column(nullable = false)
    private Integer mapy;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ReviewEntity> reviewList;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String storeAddress;

    @Column(nullable = true)
    private String image;

    public static Store fromStoreMongoEntity(StoreMongoEntity storeMongoEntity) {
        String image = (storeMongoEntity.getStoreImageList() != null && !storeMongoEntity.getStoreImageList().isEmpty())
                ? storeMongoEntity.getStoreImageList().get(0)
                : "";
        return Store.builder()
                .storeName(storeMongoEntity.getName())
                .mapx(storeMongoEntity.getMapx())
                .mapy(storeMongoEntity.getMapy())
                .storeAddress(storeMongoEntity.getAddress())
                .phoneNumber(storeMongoEntity.getPhone_number())
                .image(image)
                .build();
    }
}
