package com.study.webclientdemo;/*
 *  Copyright 2014 NHN Entertainment Corp. All rights Reserved.
 *  NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *   @author changyoun.seo@nhnedu.com
 *   @since 21. 6. 22. 오후 4:12
 *   @desc
 */

import com.study.webclientdemo.models.PostModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String KEY = "KEY";

    private final WebClient webClient;

    @Autowired
    public PostClient() {
        this.webClient = WebClient.builder()
                                  .baseUrl(BASE_URL)
                                  .filter((request, next) ->
                                                  Mono.deferContextual(contextView -> {
                                                      String value = contextView.get(KEY);

                                                      System.out.println("컨텍스트 전달완료 = " + value);

                                                      return next.exchange(request);
                                                  })
                                  )
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
                             // 구독 전파되기 떄문에, 가장 아래에 있어도 밑에서 위로 전파된다.
                             .contextWrite(context -> context.put(KEY, "ThreadLocal 등을 활용해야 했던 어떤 정보들"));
    }
}
