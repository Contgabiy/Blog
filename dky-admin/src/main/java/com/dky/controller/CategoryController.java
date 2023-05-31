package com.dky.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.dky.domain.ResponseResult;
import com.dky.domain.entity.Category;
import com.dky.domain.vo.CategoryVO;
import com.dky.domain.vo.ExcelCategoryVo;
import com.dky.domain.vo.PageVO;
import com.dky.enums.AppHttpCodeEnum;
import com.dky.service.CategoryService;
import com.dky.utils.BeanCopyUtils;
import com.dky.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        return ResponseResult.okResult(categoryService.listAllCategory());
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    /**
     * 查询所有分类
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @param status
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Long pageNum, Long pageSize, String name, String status) {
        PageVO pageVO = categoryService.listAll(pageNum, pageSize, name, status);
        return ResponseResult.okResult(pageVO);
    }

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category) {
        categoryService.save(category);
        return ResponseResult.okResult();
    }

    /**
     * 获取菜单信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getCategory(@PathVariable Long id) {
        CategoryVO categoryVO = categoryService.getCategory(id);
        return ResponseResult.okResult(categoryVO);
    }

    /**
     * 更新菜单
     * @param category
     * @return
     */
    @PutMapping
    public ResponseResult updateCategory(@RequestBody Category category) {
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    /**
     * 删除菜单（逻辑删除）
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }
}
