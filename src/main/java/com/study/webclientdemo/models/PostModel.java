package com.study.webclientdemo.models;/*
 *  Copyright 2014 NHN Entertainment Corp. All rights Reserved.
 *  NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *   @author changyoun.seo@nhnedu.com
 *   @since 21. 6. 22. 오후 3:48
 *   @desc
 */

import lombok.Builder;
import lombok.Data;

import java.beans.ConstructorProperties;

@Data
@Builder
public class PostModel {
    private Long postId;
    private String title;
    private String contents;

    @ConstructorProperties({"postId", "title", "contents"})
    public PostModel(Long postId, String title, String contents) {
        this.postId = postId;
        this.title = title;
        this.contents = contents;
    }
}
