package org.example.anibuddy.Review;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.anibuddy.store.Store;
import org.example.anibuddy.store.StoreService;
import org.example.anibuddy.user.UserEntity;
import org.example.anibuddy.user.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final StoreService storeService;

    public List<ReviewEntity> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Transactional
    public void createReview(String storeName, ReveiwCreateDto reveiwCreateDto) {

        UserEntity user = userService.getUserByNickname(reveiwCreateDto.getNickName());
        Store store = storeService.getStore(storeName);
        ReviewEntity savedReview = new ReviewEntity().builder()
                .review(reveiwCreateDto.getReview())
                .createDate(reveiwCreateDto.getVisitedTime())
                .userEntity(user)
                .storeEntity(store).build();

        List<ReviewImage> images = new ArrayList<>();
        for(String imageUrl : reveiwCreateDto.getReviewImageList()){
            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setImageUrl(imageUrl);
            images.add(reviewImage);
        }

        savedReview.setReviewImages(images);
        reviewRepository.save(savedReview);

    }
}
