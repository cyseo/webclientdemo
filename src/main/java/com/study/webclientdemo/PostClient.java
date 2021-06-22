package com.study.webclientdemo;/*
 *  Copyright 2014 NHN Entertainment Corp. All rights Reserved.
 *  NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *   @author changyoun.seo@nhnedu.com
 *   @since 21. 6. 22. 오후 4:12
 *   @desc
 */

import com.study.webclientdemo.models.PostModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class PostClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String AUTH_REQUIRED = "AUTH_REQUIRED";

    private final WebClient webClient;

    @Autowired
    public PostClient() {
        this.webClient = WebClient.builder()
                                  .baseUrl(BASE_URL)
                                  .filter(checkAuth())
                                  .build();
    }

    public Flux<PostModel> getPostList() {
        // only body
        return this.webClient.get()
                             .uri("/posts")
                             .accept(MediaType.APPLICATION_JSON)
                             .retrieve()
                             .bodyToFlux(PostModel.class);
    }

    public Mono<PostModel> getPost(final Long postId) {
        return this.webClient.get()
                             .uri("/posts/{postId}", postId)
                             .accept(MediaType.APPLICATION_JSON)
                             .attribute(AUTH_REQUIRED, "true")      // 현재 request 에만 영향을 미침
                             .retrieve()
                             .bodyToMono(PostModel.class);
    }

    private ExchangeFilterFunction checkAuth() {
        return (request, next) -> {
            final Optional<Object> isAuthRequired = request.attribute(AUTH_REQUIRED);

            if (isAuthRequired.isPresent() && Boolean.parseBoolean(isAuthRequired.get().toString())) {
                System.out.println("인증 여기서 처리");
            } else {
                System.out.println("운이 좋군..");
            }

            return next.exchange(request);
        };
    }
}
