package com.study.webclientdemo;/*
 *  Copyright 2014 NHN Entertainment Corp. All rights Reserved.
 *  NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *   @author changyoun.seo@nhnedu.com
 *   @since 21. 6. 22. 오후 3:46
 *   @desc
 */

import com.study.webclientdemo.models.PostModel;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestApiController {

    private static final List<PostModel> postList = new ArrayList<>();

    static {
        postList.add(PostModel.builder().postId(1L).title("첫글이다").contents("첫글이 내린다 샤라랄라").build());
        postList.add(PostModel.builder().postId(2L).title("비가 많이 오네요").contents("밖에 비온다 주륵주륵").build());
        postList.add(PostModel.builder().postId(3L).title("이건 테스트 에이피아이..").contents("테스트일뿐").build());
    }

    @GetMapping("/posts")
    private Flux<PostModel> getPostList() {
        return Flux.fromIterable(postList);
    }

    @GetMapping("/posts/{postId}")
    private Mono<PostModel> getPostList(@PathVariable Long postId) {
        return getPostList().filter(postModel -> postModel.getPostId().equals(postId))
                            .next();
    }

    @PostMapping("/posts")
    private Mono<PostModel> addPost(@RequestBody PostModel post) {
        postList.add(post);
        return Mono.just(post);
    }
}
