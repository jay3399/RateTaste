package com.example.ratetaste.domain.review.application.controller;

import com.example.ratetaste.domain.review.application.service.ReviewService;
import com.example.ratetaste.domain.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review saveReview = reviewService.saveReview(review);
        return new ResponseEntity<>(saveReview, HttpStatus.CREATED);
    }




}
