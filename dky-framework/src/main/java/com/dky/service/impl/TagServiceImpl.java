package com.dky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dky.domain.ResponseResult;
import com.dky.domain.dto.TagListDto;
import com.dky.domain.entity.Tag;
import com.dky.domain.vo.PageVO;
import com.dky.domain.vo.TagVO;
import com.dky.mapper.TagMapper;
import com.dky.service.TagService;
import com.dky.utils.BeanCopyUtils;
import com.dky.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-05-15 15:11:47
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVO> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());

        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        page(page, queryWrapper);

        //封装数据返回
        PageVO pageVO = new PageVO(page.getRecords(), page.getTotal());

        return ResponseResult.okResult(pageVO);
    }

    @Override
    public ResponseResult addTag(TagListDto tagListDto) {
        Tag tag = BeanCopyUtils.copyBean(tagListDto, Tag.class);
        tag.setCreateBy(SecurityUtils.getUserId());

        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTag(Long id) {
        Tag tag = getById(id);
        TagVO tagVO = BeanCopyUtils.copyBean(tag, TagVO.class);
        return ResponseResult.okResult(tagVO);
    }

    @Override
    public ResponseResult updateTag(TagVO tagVO) {
        Tag tag = getById(tagVO.getId());
        tag.setName(tagVO.getName());
        tag.setRemark(tagVO.getRemark());
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> list = list();
        List<TagVO> tagVOS = BeanCopyUtils.copyBeanList(list, TagVO.class);
        return ResponseResult.okResult(tagVOS);
    }

}

