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
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/posts")
    private Mono<PostModel> addPost() {
        return postClient.addPost();
    }
}
