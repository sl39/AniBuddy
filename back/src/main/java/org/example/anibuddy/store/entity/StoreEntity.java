package org.example.anibuddy.store.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.anibuddy.Review.entity.ReviewEntity;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="constraintName",
                        columnNames = {"storeName","address"}
                )
        }
)
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String address;

    private String roadaddress;

    @Column(columnDefinition = "TEXT")
    private String storeInfo;

    private String phoneNumber;

    private String openday;

    @Column(nullable = false)
    private int mapx;

    @Column(nullable = false)
    private int mapy;

    @JsonBackReference
    @OneToMany(mappedBy = "storeEntity", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<StoreImage> storeImageList;

    @JsonBackReference
    @OneToMany(mappedBy = "storeEntity", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ReviewEntity> reviewEntitiyList;

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "store_entity_store_category"
    )
    private List<StoreCategory> storeCategoryList;



}
