package org.example.anibuddy.Review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Setter
@Getter
@Entity
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewImageId;

    private String imageUrl;

    @ManyToOne
    @JsonBackReference
    private ReviewEntity reviewEntity;
}
