package com.dky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dky.domain.ResponseResult;
import com.dky.domain.entity.Link;
import com.dky.domain.vo.LinkVO;
import com.dky.domain.vo.PageVO;
import com.dky.mapper.LinkMapper;
import com.dky.service.LinkService;
import com.dky.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.dky.constants.SystemConstants.LINK_STATUS_NORMAL;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-05-06 09:42:30
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的友链
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, LINK_STATUS_NORMAL);
        List<Link> list = list(queryWrapper);

        //转换成vo
        List<LinkVO> linkVOS = BeanCopyUtils.copyBeanList(list, LinkVO.class);

        //封装返回
        return ResponseResult.okResult(linkVOS);
    }

    @Override
    public PageVO listAll(Long pageNum, Long pageSize, String name, String status) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Objects.nonNull(name), Link::getName, name)
                .eq(Objects.nonNull(status), Link::getStatus, status);

        Page<Link> linkPage = new Page<>(pageNum, pageSize);
        Page<Link> page = page(linkPage, queryWrapper);

        List<LinkVO> linkVOS = BeanCopyUtils.copyBeanList(page.getRecords(), LinkVO.class);
        return new PageVO(linkVOS, page.getTotal());
    }

    @Override
    public LinkVO getLinkInfo(Long id) {
        Link link = getById(id);
        LinkVO linkVO = BeanCopyUtils.copyBean(link, LinkVO.class);
        return linkVO;
    }
}

