package com.dky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dky.domain.dto.RoleDto;
import com.dky.domain.entity.Role;
import com.dky.domain.vo.PageVO;
import com.dky.domain.vo.RoleVO;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-05-16 10:39:11
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    PageVO listAllRoles(Long pageNum, Long pageSize, String roleName, String status);

    void getRoleDto(RoleVO roleVO);

    void addRole(Role role);

    RoleDto getRoleDto(Long id);

    void updateRoleInfo(Role role);

}

