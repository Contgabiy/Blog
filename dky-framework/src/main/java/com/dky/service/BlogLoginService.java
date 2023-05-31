package com.dky.service;

import com.dky.domain.ResponseResult;
import com.dky.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
