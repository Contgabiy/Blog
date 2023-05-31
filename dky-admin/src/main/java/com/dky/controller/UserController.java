package com.dky.controller;

import com.dky.domain.ResponseResult;
import com.dky.domain.entity.User;
import com.dky.domain.vo.PageVO;
import com.dky.domain.vo.UserRoleVO;
import com.dky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 查询用户
     *
     * @param pageNum
     * @param pageSize
     * @param userName
     * @param phonenumber
     * @param status
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getAllUsers(Long pageNum, Long pageSize, String userName, String phonenumber, String status) {
        PageVO pageVO = userService.getAllUsers(pageNum, pageSize, userName, phonenumber, status);
        return ResponseResult.okResult(pageVO);
    }

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    @PostMapping
    public ResponseResult addUser(@RequestBody User user) {
        userService.addUser(user);
        return ResponseResult.okResult();
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseResult.okResult();
    }

    /**
     * 用户信息回显
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getUserInfo(@PathVariable Long id) {
        UserRoleVO userRoleVO = userService.getUserInfo(id);
        return ResponseResult.okResult(userRoleVO);
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @PutMapping
    public ResponseResult updateUser(@RequestBody User user){
        userService.updateUser(user);
        return ResponseResult.okResult();
    }

}
