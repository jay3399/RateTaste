package com.example.ratetaste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// 식당이름 입력시 -> 각 사이트들의 리뷰를기반으로 ML을 이용해만든 평점을 제공

// 모델  : 많은양의 리뷰+평점 크롤링 -> 데이터전처리 o -> 벡터화 o -> 모델구축 o -> 모델훈련 -> ML 완성

// ML 적용 : 특적식당의 리뷰를 크롤링 -> ML 적용해서 , 점수산출

// 1. 개별리뷰들을 하나하나 ML 적용하여  , 점수를 산출하여 평균을 낸다  ( 개별감정 + , but 속도 --)

// 2. 모든 리뷰들의 텍스트를 합쳐보내서 ML을 적용 해서 점수를낸다 .    ( 개별감정 -- , 전체감정 ++ , 속도 ++ )

//  주기적으로 크롤링을하여 , 리뷰를 저장 + ML 산출

//  추가되는 리뷰가 있을때마다 , ML 을시행하지만 , 모든 리뷰에대해 실행하지않고 추가된것만 실행하고 , 전 점수와 함쳐서 평균낸다







@SpringBootApplication
public class RateTasteApplication {

    public static void main(String[] args) {
        SpringApplication.run(RateTasteApplication.class, args);
    }

}
