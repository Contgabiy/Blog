package com.dky.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVO {
    private Long id;

    private String content;
    //标题
    private String title;
    //文章摘要
    private String summary;
    //所属分类名
    private Long categoryId;

    private String categoryName;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
