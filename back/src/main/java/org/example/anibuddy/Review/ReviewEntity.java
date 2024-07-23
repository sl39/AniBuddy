package org.example.anibuddy.Review;

import jakarta.persistence.*;
import lombok.*;
import org.example.anibuddy.store.Store;
import org.example.anibuddy.user.UserEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @Column(length = 500)
    private String review;

    private String createDate;

    private String modifyDate;

    @ManyToOne
    private UserEntity userEntity;

    @ManyToOne
    private Store storeEntity;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages;

}
