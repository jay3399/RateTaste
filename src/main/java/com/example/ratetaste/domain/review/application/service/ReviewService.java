package com.example.ratetaste.domain.review.application.service;

import com.example.ratetaste.domain.review.domain.Review;
import com.example.ratetaste.domain.review.infrastructure.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;

    public Review saveReview(Review review) {
        return repository.save(review);
    }


}
