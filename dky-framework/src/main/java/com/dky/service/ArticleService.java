package com.dky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dky.domain.ResponseResult;
import com.dky.domain.entity.Article;
import com.dky.domain.vo.ArticleUploadVO;
import com.dky.domain.vo.PageVO;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryID);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(ArticleUploadVO articleUploadVO);


    PageVO getAllArticleList(Long pageNum, Long pageSize, String title, String summary);
}
