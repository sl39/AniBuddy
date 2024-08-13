package org.example.anibuddy.Review.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.user.UserEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String review;

    @CreatedDate
    private LocalDate createDate;

    @LastModifiedDate
    private LocalDate updateDate;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity userEntity;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(nullable = false)
    private StoreEntity storeEntity;

    @JsonBackReference
    @OneToMany(mappedBy = "reviewEntity", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ReviewImage> reviewImageList;

    @Column
    private Float reviewScore;

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "review_entity_store_tag"
    )
    private List<ReviewTag> reviewTagList;


}
