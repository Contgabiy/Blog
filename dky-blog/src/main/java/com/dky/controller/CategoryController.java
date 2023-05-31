package com.dky.controller;

import com.dky.domain.ResponseResult;
import com.dky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查找分类
     * @return ResponseResult
     */
    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        return  categoryService.getCategoryList();
    }
}
