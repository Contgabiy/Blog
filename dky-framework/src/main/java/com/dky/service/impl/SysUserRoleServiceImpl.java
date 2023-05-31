package com.dky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dky.domain.entity.SysUserRole;
import com.dky.mapper.SysUserRoleMapper;
import com.dky.service.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(SysUserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-05-28 16:15:53
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

}

