package com.study.webclientdemo;/*
 *  Copyright 2014 NHN Entertainment Corp. All rights Reserved.
 *  NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *   @author changyoun.seo@nhnedu.com
 *   @since 21. 6. 22. 오후 4:42
 *   @desc
 */

import com.study.webclientdemo.models.PostModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/webclient")
@RequiredArgsConstructor
public class PostController {

    private final PostClient postClient;

    @GetMapping("/posts")
    private Flux<PostModel> getPostList() {
        return postClient.getPostList();
    }

    @GetMapping("/posts/{postId}")
    private Mono<PostModel> getPostList(@PathVariable Long postId) {
        return postClient.getPost(postId);
    }
}
