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

@Service
public class PostClient {

    private static final String BASE_URL = "http://localhost:8080";

    private final WebClient webClient;

    @Autowired
    public PostClient() {
        this.webClient = WebClient.builder()
                                  .baseUrl(BASE_URL)
                                  .filter((request, next) -> {
                                      Mono.deferContextual(contextView -> {
                                          // 약간 ThreadLocal 느낌
                                          String value = contextView.get("postId");

                                          System.out.println("컨텍스트: postId = " + value);

                                          return Mono.empty();
                                      });

                                      return next.exchange(request);
                                  })
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
                             .bodyToMono(PostModel.class)
                             .flatMap(postModel -> {
                                 // 결과를 체이닝 하여 추가 작업을 하는 경우
                                 postModel.setContents(postModel.getContents() + " :: 추가 작업 후 결과");

                                 return Mono.just(postModel);
                             })
                             .contextWrite(context -> {
                                 // 중첩된 작업에 모두 영향을 미친다.
                                 // 따라서 체인의 가장 마지막에 위치해야한다.
                                 context.put("postId", postId);

                                 return context;
                             });
    }
}
