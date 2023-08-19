package com.example.ratetaste.domain.review.infrastructure;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ReviewCrawler {

    private static final String REVIEW_URL = "https://example-restaurant-reviews.com/reviews?restaurant=%s&page=%d";
    private static final int MAX_PAGES = 10;

    private static final List<String> SITE_URLS = Arrays.asList(
            "https://siteA.com/reviews?restaurant=%s&page=%d",
            "https://siteB.com/reviews?restaurant=%s&page=%d"
    );


    private List<String> fetchReviews() {
        List<String> reviews = new ArrayList<>();

        try {
            Document document = Jsoup.connect(REVIEW_URL).get();
            Elements reviewElements = document.select(".review-content");

            return reviewElements.stream()
                    .map(element -> element.text()).toList();

        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }


    public List<String> fetchReviewsWithParallel(String restaurantName) {
        return IntStream.rangeClosed(1, MAX_PAGES)
                .parallel()
                .mapToObj(page -> new AbstractMap.SimpleEntry<>(restaurantName, page))
                .flatMap(entry -> fetchReviewsFromPage(entry.getKey() ,entry.getValue()).stream())
                .collect(Collectors.toList());
    }

    private List<String> fetchReviewsFromPage(String restaurantName , int page) {

        try {
            String url = String.format(REVIEW_URL, restaurantName , page);
            Document document = Jsoup.connect(url).get();
            Elements reviewElement = document.select(".review-content");

            return reviewElement.stream()
                    .map(element -> element.text())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }

    }

}
