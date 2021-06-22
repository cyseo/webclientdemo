package com.study.webclientdemo;/*
 *  Copyright 2014 NHN Entertainment Corp. All rights Reserved.
 *  NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *   @author changyoun.seo@nhnedu.com
 *   @since 21. 6. 22. 오후 3:46
 *   @desc
 */

import com.study.webclientdemo.models.PostModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RestController
public class TestApiController {

    private static final List<PostModel> POST_DATA = Arrays.asList(
            PostModel.builder().postId(1L).title("첫글이다").contents("첫글이 내린다 샤라랄라").build(),
            PostModel.builder().postId(2L).title("비가 많이 오네요").contents("밖에 비온다 주륵주륵").build(),
            PostModel.builder().postId(3L).title("이건 테스트 에이피아이..").contents("테스트일뿐").build()
    );

    @GetMapping("/posts")
    private Flux<PostModel> getPostList() {
        return Flux.fromIterable(POST_DATA);
    }

    @GetMapping("/posts/{postId}")
    private Mono<PostModel> getPostList(@PathVariable Long postId) {
        return getPostList().filter(postModel -> postModel.getPostId().equals(postId))
                            .next();
    }
}
