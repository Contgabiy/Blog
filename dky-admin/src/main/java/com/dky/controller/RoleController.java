package com.dky.controller;

import com.dky.domain.ResponseResult;
import com.dky.domain.dto.RoleDto;
import com.dky.domain.entity.Role;
import com.dky.domain.vo.PageVO;
import com.dky.domain.vo.RoleVO;
import com.dky.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 查询所有角色
     *
     * @param pageNum
     * @param pageSize
     * @param roleName
     * @param status
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Long pageNum, Long pageSize, String roleName, String status) {
        PageVO pageVO = roleService.listAllRoles(pageNum, pageSize, roleName, status);
        return ResponseResult.okResult(pageVO);
    }

    /**
     * 更改角色状态
     *
     * @param roleVO
     * @return
     */
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleVO roleVO) {
        roleService.getRoleDto(roleVO);
        return ResponseResult.okResult();
    }

    /**
     * 添加角色并与菜单表绑定
     *
     * @param role
     * @return
     */
    @PostMapping
    public ResponseResult addRole(@RequestBody Role role) {
        roleService.addRole(role);
        return ResponseResult.okResult();
    }

    /**
     * 角色信息回显
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getRoleInfo(@PathVariable Long id) {
        RoleDto roleDto = roleService.getRoleDto(id);
        return ResponseResult.okResult(roleDto);
    }

    /**
     * 更新角色
     *
     * @param role
     * @return
     */
    @PutMapping
    public ResponseResult updateRole(@RequestBody Role role) {
        roleService.updateRoleInfo(role);
        return ResponseResult.okResult();
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable Long id) {
        roleService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole() {
        List<Role> list = roleService.list();
        return ResponseResult.okResult(list);
    }

}
