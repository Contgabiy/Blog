package com.dky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dky.domain.ResponseResult;
import com.dky.domain.entity.User;
import com.dky.domain.vo.PageVO;
import com.dky.domain.vo.UserRoleVO;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-05-09 11:18:16
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    PageVO getAllUsers(Long pageNum, Long pageSize, String userName, String phonenumber, String status);

    void addUser(User user);

    void deleteUser(Long id);

    UserRoleVO getUserInfo(Long id);

    void updateUser(User user);
}

