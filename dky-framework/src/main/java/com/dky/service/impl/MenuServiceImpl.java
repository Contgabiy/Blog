package com.dky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dky.constants.SystemConstants;
import com.dky.domain.ResponseResult;
import com.dky.domain.dto.MenuDto;
import com.dky.domain.entity.Menu;
import com.dky.domain.entity.SysRoleMenu;
import com.dky.domain.vo.RoleMenuVO;
import com.dky.enums.AppHttpCodeEnum;
import com.dky.mapper.MenuMapper;
import com.dky.mapper.SysRoleMenuMapper;
import com.dky.service.MenuService;
import com.dky.service.SysRoleMenuService;
import com.dky.utils.BeanCopyUtils;
import com.dky.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-05-16 10:30:00
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，则返回所有权限
        if (id == 1L) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(queryWrapper);
            return menus.stream().map(Menu::getPerms).collect(Collectors.toList());
        }
        //返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员，如果是返回所有符合要求的menu
        if (SecurityUtils.isAdmin()) {
            menus = menuMapper.selectAllRoutersMenu();

        } else {
            //否则返回当前用户所拥有的menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        List<Menu> menuTree = buildMenuTree(menus, 0L);

        return menuTree;
    }

    @Override
    public List<Menu> allMenuList(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Objects.nonNull(status), Menu::getStatus, status)
                .like(Objects.nonNull(menuName), Menu::getMenuName, menuName)
                .orderByAsc(Menu::getParentId)
                .orderByAsc(Menu::getOrderNum);

        List<Menu> list = list(queryWrapper);

        return list;
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if (menu.getParentId().equals(menu.getId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delMenu(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL)
                .eq(Menu::getParentId, menuId);

        List<Menu> menus = menuMapper.selectList(queryWrapper);

        if (menus.size() > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "存在子菜单不允许删除");
        }
        removeById(menuId);
        return ResponseResult.okResult();
    }

    @Override
    public List<MenuDto> treeSelect() {
        List<Menu> menus = menuMapper.selectAllRoutersMenu();

        List<Menu> menuTree = buildMenuTree(menus, 0L);

        List<MenuDto> list = BeanCopyUtils.copyBeanList(menuTree, MenuDto.class);
        return list;
    }

    @Override
    public RoleMenuVO roleMenuTreeselect(Long id) {
        List<MenuDto> menus = treeSelect();

        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId,id);
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(queryWrapper);

        List<Long> menuIds = sysRoleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        RoleMenuVO roleMenuVO = new RoleMenuVO(menus, menuIds);

        return roleMenuVO;
    }


    private List<Menu> buildMenuTree(List<Menu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
    }

    /**
     * 获取传入参数的子菜单
     *
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        return menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
    }
}

