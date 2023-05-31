package com.dky.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dky.domain.entity.Menu;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {
    @TableField(exist = false)
    private List<Menu> children;

    @TableId
    private Long id;

    private String menuName;

    //父菜单ID
    private Long parentId;
}
