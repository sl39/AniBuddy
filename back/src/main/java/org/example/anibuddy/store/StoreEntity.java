package org.example.anibuddy.store;

import java.util.List;
import java.util.Optional;

import org.example.anibuddy.following.FollowingEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(
		name="constraintName", columnNames = {"storeName","address"} ) } )
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
    
}
