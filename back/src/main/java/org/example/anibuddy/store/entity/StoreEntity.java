package org.example.anibuddy.store.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.anibuddy.Review.entity.ReviewEntity;
import org.example.anibuddy.owner.OwnerEntity;

import java.util.List;

@Entity
@Getter
@Setter
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

    @Column(columnDefinition = "TEXT")
    private String openday;

    @Column(nullable = false)
    private double mapx;

    @Column(nullable = false)
    private double mapy;

    @Column(nullable = true)
    private String district;


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


    @JsonBackReference
    @OneToOne(mappedBy = "storeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private StoreSummary storeSummary;

    @JsonBackReference
    @ManyToOne
    private OwnerEntity ownerEntity;

}
