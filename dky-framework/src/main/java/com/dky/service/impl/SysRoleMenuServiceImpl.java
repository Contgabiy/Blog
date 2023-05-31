package com.dky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dky.domain.entity.SysRoleMenu;
import com.dky.mapper.SysRoleMenuMapper;
import com.dky.service.SysRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(SysRoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-05-28 13:24:31
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

}

