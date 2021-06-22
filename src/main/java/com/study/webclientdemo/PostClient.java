package com.study.webclientdemo;/*
 *  Copyright 2014 NHN Entertainment Corp. All rights Reserved.
 *  NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *   @author changyoun.seo@nhnedu.com
 *   @since 21. 6. 22. 오후 4:12
 *   @desc
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PostClient {

    private static final String BASE_URL = "http://localhost:8080";

    private final WebClient webClient;

    @Autowired
    public PostClient() {
        // Case 1. create
        this.webClient = WebClient.create(BASE_URL);

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
                                  .build();
    }
}
