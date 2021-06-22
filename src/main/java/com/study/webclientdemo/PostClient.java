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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    public Mono<PostModel> addPost() {
        return this.webClient.post()
                             .uri("/posts")
                             .contentType(MediaType.APPLICATION_JSON)
                             .bodyValue(generatePost())
                             .retrieve()
                             .bodyToMono(PostModel.class);
    }

    public Mono<PostModel> addPostByForm() {
        PostModel post = generatePost();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("postId", String.valueOf(post.getPostId()));
        formData.add("title", post.getTitle());
        formData.add("contents", post.getContents());

        return this.webClient.post()
                             .uri("/posts")
                             .bodyValue(formData)   // Or .body(fromFormData("k1", "v1").with("k2", "v2"))
                             .retrieve()
                             .bodyToMono(PostModel.class);
    }

    public void multipartExample() {
//        MultipartBodyBuilder builder = new MultipartBodyBuilder();
//        builder.part("fieldPart", "fieldValue");
//        builder.part("filePart1", new FileSystemResource("...logo.png"));
//        builder.part("jsonPart", new Person("Jason"));
//        builder.part("myPart", part); // Part from a server request

//        MultiValueMap<String, HttpEntity<?>> parts = builder.build();

//        return this.webClient.post()
//                             .uri("/posts")
//                             .body(parts)
//                             .retrieve()
//                             .bodyToMono(PostModel.class);
    }

    private PostModel generatePost() {
        long postId = (long) (Math.random() * 100);
        return PostModel.builder()
                        .postId(postId)
                        .title("랜덤 글쓰기 " + postId)
                        .contents("글을 마구 쓴다")
                        .build();
    }
}
