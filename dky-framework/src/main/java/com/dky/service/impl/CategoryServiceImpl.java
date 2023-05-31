package com.dky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dky.constants.SystemConstants;
import com.dky.domain.ResponseResult;
import com.dky.domain.entity.Article;
import com.dky.domain.entity.Category;
import com.dky.domain.vo.CategoryVO;
import com.dky.domain.vo.PageVO;
import com.dky.mapper.CategoryMapper;
import com.dky.service.ArticleService;
import com.dky.service.CategoryService;
import com.dky.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-04-30 10:18:57
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表，状态为已发布的文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(queryWrapper);

        //获取文章的分类ID，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());

        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        //封装vo
        List<CategoryVO> categoryVOS = BeanCopyUtils.copyBeanList(categories, CategoryVO.class);

        return ResponseResult.okResult(categoryVOS);
    }

    @Override
    public List<CategoryVO> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, 0);
        List<Category> categoryList = list(queryWrapper);

        return BeanCopyUtils.copyBeanList(categoryList, CategoryVO.class);
    }

    @Override
    public PageVO listAll(Long pageNum, Long pageSize, String name, String status) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Objects.nonNull(name), Category::getName, name)
                .eq(Objects.nonNull(status), Category::getStatus, status);

        Page<Category> categoryPage = new Page<>(pageNum, pageSize);
        Page<Category> page = page(categoryPage, queryWrapper);

        PageVO pageVO = new PageVO(page.getRecords(), page.getTotal());
        return pageVO;
    }

    @Override
    public CategoryVO getCategory(Long id) {
        Category category = getById(id);
        CategoryVO categoryVO = BeanCopyUtils.copyBean(category, CategoryVO.class);
        return categoryVO;
    }
}

