package org.example.anibuddy.Review.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.anibuddy.Review.dto.ReviewCreateDto;
import org.example.anibuddy.Review.entity.ReviewEntity;
import org.example.anibuddy.Review.entity.ReviewImage;
import org.example.anibuddy.Review.repository.ReviewImageRepository;
import org.example.anibuddy.Review.repository.ReviewRepository;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.service.StoreService;
import org.example.anibuddy.user.UserEntity;
import org.example.anibuddy.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final StoreService storeService;
    private final UserService userService;


    public List<ReviewEntity> getAll() {
        return reviewRepository.findAll();
    }

    @Transactional
    public void createReview(ReviewCreateDto reviewCreateDto) throws Exception {
        Optional<StoreEntity> storeEntity = storeService.getStore(reviewCreateDto.getStoreName(), reviewCreateDto.getStoreAddress());
        Optional<UserEntity> userEntity = userService.getUserByNickname(reviewCreateDto.getNickName());

        if (!storeEntity.isPresent()) {
            throw new Exception("스토어가 존재하지 않습니다");
        }

        if (!userEntity.isPresent()) {
            throw new Exception("유저가 존재하지 않습니다");
        }

        ReviewEntity reviewEntity = new ReviewEntity().builder()
                .review(reviewCreateDto.getReview())
                .userEntity(userEntity.get())
                .storeEntity(storeEntity.get())
                .createDate(parseDate(reviewCreateDto.getVisitedTime()))
                .build();

        reviewRepository.save(reviewEntity);
        List<ReviewImage> reviewImageList = new ArrayList<>();
        for(String imageUrl : reviewCreateDto.getReviewImageList()){
            ReviewImage reviewImage = new ReviewImage().builder()
                    .imageUrl(imageUrl)
                    .reviewEntity(reviewEntity)
                    .build();
            reviewImageList.add(reviewImage);
        }
        reviewImageRepository.saveAll(reviewImageList);

    }

    public static LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 E요일", Locale.KOREAN);
        return LocalDate.parse(dateString, formatter);

    }
}
