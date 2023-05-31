package com.dky.service.impl;

import com.dky.domain.ResponseResult;
import com.dky.domain.entity.LoginUser;
import com.dky.domain.entity.User;
import com.dky.domain.vo.BlogLoginVO;
import com.dky.domain.vo.UserInfoVo;
import com.dky.service.BlogLoginService;
import com.dky.utils.BeanCopyUtils;
import com.dky.utils.JwtUtil;
import com.dky.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误！");
        }

        //获取userID，生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long id = loginUser.getUser().getId();
        String jwt = JwtUtil.createJWT(String.valueOf(id));

        //把用户信息存入redis
        redisCache.setCacheObject("bloglogin:" + id, loginUser);

        //把token和userinfo封装返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);

        BlogLoginVO blogLoginVO = new BlogLoginVO(jwt, userInfoVo);
        return ResponseResult.okResult(blogLoginVO);
    }

    @Override
    public ResponseResult logout() {
        //获取token，解析userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        //获取userid
        Long userId = loginUser.getUser().getId();

        //删除redis中的用户信息
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
