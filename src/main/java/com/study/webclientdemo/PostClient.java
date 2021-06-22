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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
        final List<PostModel> postList = this.webClient.get()
                                                       .uri("/posts")
                                                       .accept(MediaType.APPLICATION_JSON)
                                                       .retrieve()
                                                       .bodyToFlux(PostModel.class)
                                                       .collectList()
                                                       .block();    // 동기적으로 사용해야 하는 경우

        return this.webClient.get()
                             .uri("/posts")
                             .accept(MediaType.APPLICATION_JSON)
                             .retrieve()
                             .bodyToFlux(PostModel.class);
    }

    public Mono<PostModel> getPost(final Long postId) {
        final PostModel post = this.webClient.get()
                                             .uri("/posts/{postId}", postId)
                                             .accept(MediaType.APPLICATION_JSON)
                                             .retrieve()
                                             .bodyToMono(PostModel.class)
                                             .block();

        return this.webClient.get()
                             .uri("/posts/{postId}", postId)
                             .accept(MediaType.APPLICATION_JSON)
                             .retrieve()
                             .bodyToMono(PostModel.class);
    }
}
