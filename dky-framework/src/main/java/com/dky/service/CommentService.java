package com.dky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dky.domain.ResponseResult;
import com.dky.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-05-08 18:53:20
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

