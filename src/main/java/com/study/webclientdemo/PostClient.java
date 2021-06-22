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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostClient {

    private static final String BASE_URL = "http://localhost:8080";

    private final WebClient webClient;

    @Autowired
    public PostClient() {
        // Case 1. create
//        this.webClient = WebClient.create(BASE_URL);

        // Case 2. Builder
        this.webClient = WebClient.builder()
                                  .baseUrl(BASE_URL)
                                  .defaultHeader(HttpHeaders.USER_AGENT, "USER_AGENT")
                                  // .uriBuilderFactory()      // Customized UriBuilderFactory to use as a base URL.
                                  // .defaultUriVariables()    // default values to use when expanding URI templates.
                                  // .defaultHeader()          // Headers for every request.
                                  // .defaultCookie()          // Cookies for every request.
                                  // .defaultRequest()         // Consumer to customize every request.
                                  // .filter()                 // Client filter for every request.
                                  // .exchangeStrategies()     // HTTP message reader/writer customizations.
                                  // .clientConnector()        // HTTP client library settings.
                                  .filter((request, next) -> next.exchange(request))
                                  .filter(logRequest())
                                  .filters(filterList -> {
                                      filterList.add(0, logRequest());
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

    private ExchangeFilterFunction logRequest() {
        return (request, next) -> {
            System.out.println("Request: " + request.method() + ", " + request.url());

            request.headers()
                   .forEach((name, values) -> values.forEach(value -> System.out.println(name + " = " + value)));

            return next.exchange(request);
        };
    }
}
