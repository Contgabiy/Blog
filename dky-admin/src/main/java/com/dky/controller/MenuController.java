package com.dky.controller;

import com.dky.domain.ResponseResult;
import com.dky.domain.dto.MenuDto;
import com.dky.domain.entity.Menu;
import com.dky.domain.vo.MenuVO;
import com.dky.domain.vo.RoleMenuVO;
import com.dky.service.MenuService;
import com.dky.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 查询所有菜单
     *
     * @param status
     * @param menuName
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(String status, String menuName) {
        List<Menu> menuList = menuService.allMenuList(status, menuName);
        return ResponseResult.okResult(menuList);
    }

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu) {
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    /**
     * 查询菜单
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getMenu(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        MenuVO menuVO = BeanCopyUtils.copyBean(menu, MenuVO.class);
        return ResponseResult.okResult(menuVO);
    }

    /**
     * 更新菜单
     *
     * @param menu
     * @return
     */
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }

    /**
     * 删除菜单
     *
     * @param menuId
     * @return
     */
    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable Long menuId) {
        return menuService.delMenu(menuId);
    }

    /**
     * 添加角色显示菜单
     *
     * @return
     */
    @GetMapping("/treeselect")
    public ResponseResult treeSelect() {
        List<MenuDto> list = menuService.treeSelect();
        return ResponseResult.okResult(list);
    }

    /**
     *  查询角色对应菜单
     * @param id
     * @return
     */
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable Long id) {
        RoleMenuVO roleMenuVO = menuService.roleMenuTreeselect(id);
        return ResponseResult.okResult(roleMenuVO);
    }
}
