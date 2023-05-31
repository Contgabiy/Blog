package com.dky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dky.domain.ResponseResult;
import com.dky.domain.dto.MenuDto;
import com.dky.domain.entity.Menu;
import com.dky.domain.vo.RoleMenuVO;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-05-16 10:30:00
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> allMenuList(String status, String menuName);

    ResponseResult updateMenu(Menu menu);

    ResponseResult delMenu(Long menuId);

    List<MenuDto> treeSelect();

    RoleMenuVO roleMenuTreeselect(Long id);
}

