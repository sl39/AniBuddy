package org.example.anibuddy.Review.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.anibuddy.Review.dto.ReviewCreateDto;
import org.example.anibuddy.Review.dto.ReviewDetailResponseDto;
import org.example.anibuddy.Review.entity.ReviewEntity;
import org.example.anibuddy.Review.entity.ReviewImage;
import org.example.anibuddy.Review.entity.ReviewTag;
import org.example.anibuddy.Review.repository.ReviewImageRepository;
import org.example.anibuddy.Review.repository.ReviewRepository;
import org.example.anibuddy.Review.repository.ReviewTagRepository;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.repository.StoreRepository;
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
    private final ReviewTagRepository reviewTagRepository;
    private final StoreRepository storeRepository;


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

        List<ReviewTag> tags = new ArrayList<>();

        for(String tag : reviewCreateDto.getTags()) {
            Optional<ReviewTag> getTag = reviewTagRepository.findByTag(tag);
            if (!getTag.isPresent()) {
                continue;
            }
            tags.add(getTag.get());
        }


        ReviewEntity reviewEntity = new ReviewEntity().builder()
                .review(reviewCreateDto.getReview())
                .userEntity(userEntity.get())
                .storeEntity(storeEntity.get())
                .createDate(parseDate(reviewCreateDto.getVisitedTime()))
                .reviewTagList(tags)
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

    @Transactional
    public void createReviewAll(List<ReviewCreateDto> reviewCreateDtoList) throws Exception {
        for (ReviewCreateDto reviewCreateDto : reviewCreateDtoList) {
            createReview(reviewCreateDto);
        }
    }
    @Transactional
    public void saveTags(List<String> tags) {
        List<ReviewTag> reviewTagList = new ArrayList<>();
        for(String tag : tags) {
            ReviewTag reviewTag = new ReviewTag().builder()
                    .tag(tag)
                    .build();
            reviewTagRepository.save(reviewTag);

        }
    }

    public List<ReviewDetailResponseDto> getReviews(Integer storeId) throws Exception {
        Optional<StoreEntity> storeEntity = storeRepository.findById(storeId);
        if (storeEntity.isEmpty()) {
            throw new Exception("스토어가 존재하지 않습니다");
        }
        List<ReviewEntity> reviews = reviewRepository.findByStoreEntity(storeEntity.get());
        List<ReviewDetailResponseDto> reviewDetailResponseDtoList = new ArrayList<>();
        for (ReviewEntity reviewEntity : reviews) {
            List<String> images = new ArrayList<String>();
            for(ReviewImage reviewImage : reviewEntity.getReviewImageList()){
                images.add(reviewImage.getImageUrl());
            }
            ReviewDetailResponseDto reviewDetailResponseDto = ReviewDetailResponseDto.builder()
                    .reviewScore(reviewEntity.getReviewScore())
                    .reviewId(reviewEntity.getId())
                    .review(reviewEntity.getReview())
                    .createDate(reviewEntity.getCreateDate().toString())
                    .updateDate(reviewEntity.getUpdateDate() != null ? reviewEntity.getUpdateDate().toString() : null)
                    .reviewImageList(images)
                    .build();
            reviewDetailResponseDtoList.add(reviewDetailResponseDto);
        }

        return reviewDetailResponseDtoList;
    }
}
