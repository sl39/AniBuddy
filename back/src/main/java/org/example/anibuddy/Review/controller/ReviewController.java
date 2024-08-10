package org.example.anibuddy.Review.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.Review.dto.ReviewCreateDto;
import org.example.anibuddy.Review.entity.ReviewEntity;
import org.example.anibuddy.Review.entity.ReviewTag;
import org.example.anibuddy.Review.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/all")
    public List<ReviewEntity> getAllReview(){
        return reviewService.getAll();
    }

    @PostMapping("/create")
    public String createReview(@RequestBody ReviewCreateDto reviewCreateDto) throws Exception {
        reviewService.createReview(reviewCreateDto);
        return "test";
    }

    @PostMapping("/createAll")
    public String createAllReview(@RequestBody List<ReviewCreateDto> reviewCreateDtoList) throws Exception {
        reviewService.createReviewAll(reviewCreateDtoList);
        return "test";
    }

    @PostMapping("/creatTags")
    public String creatTags(@RequestBody List<String> tags) throws Exception {
        reviewService.saveTags(tags);
        return "test";
    }
}
