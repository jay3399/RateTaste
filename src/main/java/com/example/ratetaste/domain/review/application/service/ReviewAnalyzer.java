package com.example.ratetaste.domain.review.application.service;

import com.example.ratetaste.domain.review.infrastructure.ReviewCrawler;
import com.example.ratetaste.domain.review.infrastructure.TripAdvisorCrawler;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.stemmer.PorterStemmer;


public class ReviewAnalyzer {

    private static final Set<String> STOP_WORDS = Set.of("the", "is", "and", "...");

    public void getPredictedRatingForRestaurant(String name)
            throws ExecutionException, InterruptedException {

        TripAdvisorCrawler tripAdvisorCrawler = new TripAdvisorCrawler();

        List<String> rawReviews = tripAdvisorCrawler.fetchReviewsWithParallel();

        // 각 리뷰에 대한 처리

        for (String rawReview : rawReviews) {

            // 2. 데이터 전처리

            String preprocess = preprocess(rawReview);

            // 3. ML 모델로 평점 예측

            double predictRating = predictRating(preprocess);

        }

    }

    public static String preprocess(String text) {

        text = text.toLowerCase();
        text = text.replaceAll("[^a-z0-9 ]", "");

        String[] tokens = WhitespaceTokenizer.INSTANCE.tokenize(text);
        PorterStemmer stemmer = new PorterStemmer();

        return Arrays.stream(tokens)
                .filter(token -> !STOP_WORDS.contains(token))
                .map(stemmer::stem).collect(Collectors.joining(" "));

    }
    public static double predictRating(String review) {
        // 여기서는 임의의 로직으로 평점을 예측. 실제로는 ML 라이브러리를 사용
        return review.length() % 5 + 1; // 예시로 1~5 사이의 임의의 평점 반환
    }



}
