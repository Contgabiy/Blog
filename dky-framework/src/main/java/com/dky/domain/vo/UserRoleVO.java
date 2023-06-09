package com.dky.domain.vo;

import com.dky.domain.entity.Role;
import com.dky.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleVO {
    private List<Long> roleIds;
    private List<Role> roles;
    private UserInfoVo user;
}
