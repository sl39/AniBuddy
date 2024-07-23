package org.example.anibuddy.Review;


import lombok.RequiredArgsConstructor;
import org.example.anibuddy.store.storeMongo.ReviewMongoDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/all")
    public List<ReviewEntity> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @PostMapping("/create/{storeName}")
    public String createReview(@PathVariable(value = "storeName") String storeName, @RequestBody ReveiwCreateDto reveiwCreateDto) {
        System.out.println(storeName);
        reviewService.createReview(storeName,reveiwCreateDto);
        return "성공";
    }
}
