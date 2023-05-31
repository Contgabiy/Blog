package com.dky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dky.constants.SystemConstants;
import com.dky.domain.ResponseResult;
import com.dky.domain.entity.Comment;
import com.dky.domain.vo.CommentVO;
import com.dky.domain.vo.PageVO;
import com.dky.enums.AppHttpCodeEnum;
import com.dky.exception.SystemException;
import com.dky.mapper.CommentMapper;
import com.dky.service.CommentService;
import com.dky.service.UserService;
import com.dky.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-05-08 18:53:20
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getRootId, -1);

        //评论类型
        queryWrapper.eq(Comment::getType,commentType);

        //分页查询
        Page<Comment> commentPage = new Page<>(pageNum, pageSize);
        page(commentPage, queryWrapper);

        List<CommentVO> commentVOS = toCommentVOList(commentPage.getRecords());

        //查询所有根评论对应的子评论集合
        for (CommentVO commentVO : commentVOS) {
            //查询对应的子评论
            List<CommentVO> children = getChildren(commentVO.getId());

            commentVO.setChildren(children);
        }

        return ResponseResult.okResult(new PageVO(commentVOS, commentPage.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    private List<CommentVO> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);
        queryWrapper.orderByAsc(Comment::getCreateTime);

        List<Comment> list = list(queryWrapper);

        return toCommentVOList(list);
    }

    private List<CommentVO> toCommentVOList(List<Comment> list) {
        List<CommentVO> commentVOS = BeanCopyUtils.copyBeanList(list, CommentVO.class);

        commentVOS.stream().forEach(commentVO -> {
            //通过createBy查询用户昵称并赋值
            String nickName = userService.getById(commentVO.getCreateBy()).getNickName();
            commentVO.setUsername(nickName);

            //通过toCommentUserId查询用户的昵称并赋值
            if (commentVO.getToCommentUserId() != -1) {
                String toCommentUserName = userService.getById(commentVO.getToCommentUserId()).getNickName();
                commentVO.setToCommentUserName(toCommentUserName);
            }
        });

        return commentVOS;
    }
}

