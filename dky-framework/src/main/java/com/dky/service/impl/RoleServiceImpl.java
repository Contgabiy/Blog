package com.dky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dky.domain.dto.RoleDto;
import com.dky.domain.entity.Role;
import com.dky.domain.entity.SysRoleMenu;
import com.dky.domain.vo.PageVO;
import com.dky.domain.vo.RoleVO;
import com.dky.mapper.RoleMapper;
import com.dky.mapper.SysRoleMenuMapper;
import com.dky.service.RoleService;
import com.dky.service.SysRoleMenuService;
import com.dky.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-05-16 10:39:11
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员
        if (id == 1L) {
            ArrayList<String> roleList = new ArrayList<>();
            roleList.add("admin");
            return roleList;
        }
        //否则就查询该用户信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public PageVO listAllRoles(Long pageNum, Long pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(roleName), Role::getRoleName, roleName)
                .eq(StringUtils.hasText(status), Role::getStatus, status)
                .orderByAsc(Role::getRoleSort);

        Page<Role> page = new Page<>(pageNum, pageSize);
        Page<Role> rolePage = page(page, queryWrapper);

        PageVO pageVO = new PageVO(rolePage.getRecords(), rolePage.getTotal());
        return pageVO;
    }

    @Override
    public void getRoleDto(RoleVO roleVO) {
        Role role = roleMapper.selectById(roleVO.getRoleId());
        role.setStatus(roleVO.getStatus());
        updateById(role);
    }

    @Override
    public void addRole(Role role) {
        save(role);

        for (Long menuId : role.getMenuIds()) {
            sysRoleMenuMapper.insert(new SysRoleMenu(role.getId(), menuId));
        }
    }

    @Override
    public RoleDto getRoleDto(Long id) {
        Role role = getById(id);
        updateById(role);
        RoleDto roleDto = BeanCopyUtils.copyBean(role, RoleDto.class);
        return roleDto;
    }

    @Override
    public void updateRoleInfo(Role role) {
        updateById(role);
        List<Long> menuIds = role.getMenuIds();

        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, role.getId());
        sysRoleMenuMapper.delete(queryWrapper);

        for (Long menuId : menuIds) {
            sysRoleMenuMapper.insert(new SysRoleMenu(role.getId(), menuId));
        }

    }

}

