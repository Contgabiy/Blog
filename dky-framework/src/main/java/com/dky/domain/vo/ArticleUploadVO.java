package com.dky.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUploadVO {

    private String title;
    private String thumbnail;
    private String isTop;
    private String isComment;
    private String content;
    private List<Long> tags;
    private Long categoryId;
    private String summary;
    private String status;
}
