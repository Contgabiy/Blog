package com.dky.controller;

import com.dky.domain.ResponseResult;
import com.dky.domain.entity.Article;
import com.dky.domain.vo.ArticleDetailVO;
import com.dky.domain.vo.ArticleUploadVO;
import com.dky.domain.vo.PageVO;
import com.dky.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 添加文章
     *
     * @param articleUploadVO 前端返回请求体中的文章数据
     * @return
     */
    @PostMapping
    public ResponseResult addArticle(@RequestBody ArticleUploadVO articleUploadVO) {
        return articleService.addArticle(articleUploadVO);
    }

    /**
     * 查询所有文章
     *
     * @param pageNum
     * @param pageSize
     * @param title
     * @param summary
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Long pageNum, Long pageSize, String title, String summary) {
        PageVO pageVO = articleService.getAllArticleList(pageNum, pageSize, title, summary);
        return ResponseResult.okResult(pageVO);
    }

    /**
     * 根据id查询文章信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getById(@PathVariable Long id) {
        Article article = articleService.getById(id);
        return ResponseResult.okResult(article);
    }

    /**
     * 更新修改后的文章
     *
     * @param article
     * @return
     */
    @PutMapping
    public ResponseResult updateArticle(@RequestBody Article article) {
        articleService.updateById(article);
        return ResponseResult.okResult();
    }

    /**
     * 删除文章（逻辑删除）
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable Long id) {
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}
