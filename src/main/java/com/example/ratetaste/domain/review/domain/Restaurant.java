package com.example.ratetaste.domain.review.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double raging;


    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviewList = new ArrayList<>();


    public void addReview(Review review) {
        this.reviewList.add(review);
    }






}
