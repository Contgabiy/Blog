package com.dky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dky.domain.ResponseResult;
import com.dky.domain.dto.TagListDto;
import com.dky.domain.entity.Tag;
import com.dky.domain.vo.PageVO;
import com.dky.domain.vo.TagVO;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-05-15 15:11:47
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVO> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(TagListDto tagListDto);

    ResponseResult<TagVO> getTag(Long id);

    ResponseResult updateTag(TagVO tagVO);

    ResponseResult listAllTag();
}

