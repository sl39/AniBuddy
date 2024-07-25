package org.example.anibuddy.store.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JsonBackReference
    private StoreEntity storeEntity;

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
}
