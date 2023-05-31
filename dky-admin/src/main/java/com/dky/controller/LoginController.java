package com.dky.controller;

import com.dky.domain.ResponseResult;
import com.dky.domain.entity.LoginUser;
import com.dky.domain.entity.Menu;
import com.dky.domain.entity.User;
import com.dky.domain.vo.AdminUserInfoVO;
import com.dky.domain.vo.RoutersVo;
import com.dky.domain.vo.UserInfoVo;
import com.dky.enums.AppHttpCodeEnum;
import com.dky.exception.SystemException;
import com.dky.service.LoginService;
import com.dky.service.MenuService;
import com.dky.service.RoleService;
import com.dky.utils.BeanCopyUtils;
import com.dky.utils.RedisCache;
import com.dky.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisCache redisCache;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVO> getInfo() {
        //获取当前登陆的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();

        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());

        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        //封装返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);

        AdminUserInfoVO adminUserInfoVO = new AdminUserInfoVO(perms, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVO);
    }

    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters() {
        Long userId = SecurityUtils.getUserId();
        //查询menu，如果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }

}
