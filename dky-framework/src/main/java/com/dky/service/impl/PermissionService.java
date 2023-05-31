package com.dky.service.impl;

import com.dky.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
    /**
     * 判断当前用户是否具有permission
     *
     * @param permission 要判断的权限
     * @return 是否具有权限
     */
    public boolean hasPermission(String permission) {
        //如果是超级管理员，则直接放行
        if (SecurityUtils.isAdmin()) {
            return true;
        }
        List<String> permissons = SecurityUtils.getLoginUser().getPermissons();
        return permissons.contains(permission);
    }
}
