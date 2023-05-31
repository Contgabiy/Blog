package com.dky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dky.domain.ResponseResult;
import com.dky.domain.entity.Article;
import com.dky.domain.entity.ArticleTag;
import com.dky.domain.entity.Category;
import com.dky.domain.vo.*;
import com.dky.mapper.ArticleMapper;
import com.dky.service.ArticleService;
import com.dky.service.ArticleTagService;
import com.dky.service.CategoryService;
import com.dky.utils.BeanCopyUtils;
import com.dky.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.dky.constants.SystemConstants.ARTICLE_STATUS_NORMAL;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    @Lazy
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //1.必须是正式文章
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        //2.按照浏览量降序排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //3.最多只能查出来10篇文章
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();

        //bean拷贝
//        ArrayList<HotArticleVo> articleVos = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article, vo);
//            articleVos.add(vo);
//        }

        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryID) {

        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        /*
        1.如果有categoryID，查询时要传入categoryID
        2.状态为正式发布的文章
        3.置顶的文章要显示在最前面
         */
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL).orderByDesc(Article::getIsTop);
        queryWrapper.eq(Objects.nonNull(categoryID) && categoryID > 0, Article::getCategoryId, categoryID);

        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        //查询categoryName
        List<Article> articles = page.getRecords()
                .stream()
                .peek(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        //封装查询结果VO
        List<ArticleListVO> articleListVOS = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVO.class);

        PageVO pageVO = new PageVO(articleListVOS, page.getTotal());

        return ResponseResult.okResult(pageVO);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);

        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());

        //转换成VO
        ArticleDetailVO articleDetailVO = BeanCopyUtils.copyBean(article, ArticleDetailVO.class);

        //根据分类id查询分类名
        Category category = categoryService.getById(articleDetailVO.getCategoryId());
        if (category != null) {
            articleDetailVO.setCategoryName(category.getName());
        }

        //封装响应数据
        return ResponseResult.okResult(articleDetailVO);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount", String.valueOf(id), 1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addArticle(ArticleUploadVO articleUploadVO) {
        Article article = BeanCopyUtils.copyBean(articleUploadVO, Article.class);
        save(article);

        List<ArticleTag> articleTags = articleUploadVO.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public PageVO getAllArticleList(Long pageNum, Long pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        queryWrapper.like(Objects.nonNull(title), Article::getTitle, title);
        queryWrapper.like(Objects.nonNull(summary), Article::getSummary, summary);

        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        PageVO pageVO = new PageVO(page.getRecords(), page.getTotal());
        return pageVO;
    }

}

