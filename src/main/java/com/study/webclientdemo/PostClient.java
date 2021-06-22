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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostClient {

    private static final String BASE_URL = "http://localhost:8080";

    private final WebClient webClient;

    @Autowired
    public PostClient() {
        this.webClient = WebClient.builder()
                                  .baseUrl(BASE_URL)
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
                             .retrieve()
                             .bodyToMono(PostModel.class);
    }

    public Mono<ResponseEntity<PostModel>> getPostResponseEntity(final Long postId) {
        // get Response entity
        return this.webClient.get()
                             .uri("/posts/{postId}", postId)
                             .accept(MediaType.APPLICATION_JSON)
                             .retrieve()
                             .toEntity(PostModel.class);
    }

    public Mono<PostModel> getPostWithHttpError(final Long postId) {
        return this.webClient.get()
                             .uri("/posts/{postId}", postId)
                             .accept(MediaType.APPLICATION_JSON)
                             .retrieve()
//                             .onStatus(HttpStatus::is4xxClientError, response -> ...)
//                             .onStatus(HttpStatus::is5xxServerError, response -> ...)
                             .bodyToMono(PostModel.class);
    }

    public Mono<PostModel> getPostWithExchange(final Long postId) {
        return this.webClient.get()
                             .uri("/posts/{postId}", postId)
                             .accept(MediaType.APPLICATION_JSON)
                             .exchangeToMono(response -> {
                                 if (response.statusCode().equals(HttpStatus.OK)) {
                                     return response.bodyToMono(PostModel.class);
                                 } else if (response.statusCode().is4xxClientError()) {
                                     return Mono.empty();
                                 } else {
                                     return response.createException().flatMap(Mono::error);
                                 }
                             });
    }
}
