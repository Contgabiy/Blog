package com.dky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dky.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-16 10:39:11
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}

