package com.dky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dky.domain.ResponseResult;
import com.dky.domain.entity.Link;
import com.dky.domain.vo.LinkVO;
import com.dky.domain.vo.PageVO;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-05-06 09:42:30
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    PageVO listAll(Long pageNum, Long pageSize, String name, String status);

    LinkVO getLinkInfo(Long id);
}

