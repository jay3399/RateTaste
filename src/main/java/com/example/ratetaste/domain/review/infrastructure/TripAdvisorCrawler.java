package com.example.ratetaste.domain.review.infrastructure;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TripAdvisorCrawler {

    private static final String REVIEW_URL = "https://www.tripadvisor.co.kr/Restaurant_Review-g294197-d14803574-Reviews-or%s-Flavors-Seoul.html";
    private static final int MAX_PAGES = 3;  // 적절한 페이지 수로 조절 가능

    public List<String> fetchReviewsWithParallel() throws ExecutionException, InterruptedException {

        ForkJoinPool customThreadPool = new ForkJoinPool(2);

        return customThreadPool.submit(() -> IntStream.rangeClosed(0, (MAX_PAGES - 1) * 10)
                .parallel()
                .mapToObj(page -> fetchReviewsFromPage(page))
                .flatMap(List::stream)
                .collect(Collectors.toList())).get();
    }

    private List<String> fetchReviewsFromPage(int offset) {
        System.setProperty("webdriver.chrome.driver", "/Users/jay/Downloads/chromedriver");  // 여기에 chromedriver의 실제 경로를 입력

        WebDriver driver = new ChromeDriver();
        String url = String.format(REVIEW_URL, offset);
        driver.get(url);

        List<String> reviews = new ArrayList<>();

        try {
            // '더보기' 버튼을 클릭
            List<WebElement> moreButtons = driver.findElements(By.cssSelector("span.taLnk.ulBlueLinks"));
            for (WebElement moreButton : moreButtons) {
                try {
                    moreButton.click();
                } catch (Exception e) {
                    // 무시: 버튼을 클릭할 수 없는 경우
                }
            }
            Thread.sleep(3000);  // 페이지 로드를 위해 잠시 대기

            // 리뷰 텍스트 가져오기
            List<WebElement> reviewElements = driver.findElements(By.cssSelector("div.reviewSelector div.entry p.partial_entry"));
            for (WebElement reviewElement : reviewElements) {
                reviews.add(reviewElement.getText());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return reviews;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        TripAdvisorCrawler crawler = new TripAdvisorCrawler();
        List<String> reviews = crawler.fetchReviewsWithParallel();
        reviews.forEach(System.out::println);
    }
}
