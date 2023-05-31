package com.dky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dky.domain.ResponseResult;
import com.dky.domain.entity.Article;
import com.dky.domain.entity.ArticleTag;
import com.dky.domain.vo.ArticleUploadVO;
import com.dky.mapper.ArticleTagMapper;
import com.dky.service.ArticleTagService;
import com.dky.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章标签关联表(DkyArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-05-24 17:01:55
 */
@Service("ArticleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}

