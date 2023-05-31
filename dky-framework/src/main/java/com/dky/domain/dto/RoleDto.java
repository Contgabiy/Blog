package com.dky.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    @TableId
    private Long id;

    //角色名称
    private String roleName;
    //角色权限字符串
    private String roleKey;
    private Integer roleSort;
    //角色状态（0正常 1停用）
    private String status;
    private String remark;
}