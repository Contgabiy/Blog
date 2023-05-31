package com.dky.service;

import com.dky.domain.ResponseResult;
import com.dky.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
