package com.dky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dky.domain.ResponseResult;
import com.dky.domain.entity.Role;
import com.dky.domain.entity.SysUserRole;
import com.dky.domain.entity.User;
import com.dky.domain.vo.PageVO;
import com.dky.domain.vo.UserInfoVo;
import com.dky.domain.vo.UserRoleVO;
import com.dky.enums.AppHttpCodeEnum;
import com.dky.exception.SystemException;
import com.dky.mapper.RoleMapper;
import com.dky.mapper.SysUserRoleMapper;
import com.dky.mapper.UserMapper;
import com.dky.service.RoleService;
import com.dky.service.UserService;
import com.dky.utils.BeanCopyUtils;
import com.dky.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-05-09 11:18:16
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户ID
        Long userId = SecurityUtils.getUserId();
        //根据ID查询用户信息
        User user = getById(userId);

        UserInfoVo userInfoVO = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        return ResponseResult.okResult(userInfoVO);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //对数据是否存在的判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //对密码进行加密处理
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public PageVO getAllUsers(Long pageNum, Long pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Objects.nonNull(userName), User::getUserName, userName)
                .eq(Objects.nonNull(phonenumber), User::getPhonenumber, phonenumber)
                .eq(Objects.nonNull(status), User::getStatus, status);

        Page<User> userPage = new Page<>(pageNum, pageSize);
        Page<User> page = page(userPage, queryWrapper);

        return new PageVO(page.getRecords(), page.getTotal());

    }

    @Override
    public void addUser(User user) {
        register(user);
        List<Long> roleIds = user.getRoleIds();

        for (Long id : roleIds) {
            sysUserRoleMapper.insert(new SysUserRole(user.getId(), id));
        }
    }

    @Override
    public void deleteUser(Long id) {
        Long loginId = SecurityUtils.getLoginUser().getUser().getId();
        if (loginId.equals(id)) {
            throw new SystemException(AppHttpCodeEnum.DELETE_ERROR);
        }
        removeById(id);
    }

    @Override
    public UserRoleVO getUserInfo(Long id) {
        List<Role> roles = roleMapper.selectList(null);
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, id);

        List<SysUserRole> roleIds = sysUserRoleMapper.selectList(queryWrapper);
        List<Long> collect = roleIds.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        User user = getById(id);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        return new UserRoleVO(collect, roles, userInfoVo);
    }

    @Override
    public void updateUser(User user) {
        List<Long> roleIds = user.getRoleIds();
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId,user.getId());

        sysUserRoleMapper.delete(queryWrapper);

        for (Long id : roleIds) {
            sysUserRoleMapper.insert(new SysUserRole(user.getId(), id));
        }
        updateById(user);
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);

        return count(queryWrapper) > 0;
    }

}

