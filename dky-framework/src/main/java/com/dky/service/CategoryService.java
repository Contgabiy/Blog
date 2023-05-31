package com.dky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dky.domain.ResponseResult;
import com.dky.domain.entity.Category;
import com.dky.domain.vo.CategoryVO;
import com.dky.domain.vo.PageVO;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-04-30 10:18:57
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVO> listAllCategory();

    PageVO listAll(Long pageNum, Long pageSize, String name, String status);

    CategoryVO getCategory(Long id);
}

