package com.dky.controller;

import com.dky.domain.ResponseResult;
import com.dky.domain.entity.Link;
import com.dky.domain.vo.LinkVO;
import com.dky.domain.vo.PageVO;
import com.dky.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    /**
     * 获取友链列表
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @param status
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Long pageNum, Long pageSize, String name, String status) {
        PageVO pageVO = linkService.listAll(pageNum, pageSize, name, status);
        return ResponseResult.okResult(pageVO);
    }

    /**
     * 添加友链
     *
     * @param link
     * @return
     */
    @PostMapping
    public ResponseResult addLink(@RequestBody Link link) {
        linkService.save(link);
        return ResponseResult.okResult();
    }

    /**
     * 获取友链信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getLinkInfo(@PathVariable Long id) {
        LinkVO linkVO = linkService.getLinkInfo(id);
        return ResponseResult.okResult(linkVO);
    }

    /**
     * 更新友链
     * @param link
     * @return
     */
    @PutMapping
    public ResponseResult updateLink(@RequestBody Link link) {
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    /**
     * 删除友链
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id) {
        linkService.removeById(id);
        return ResponseResult.okResult();
    }
}
