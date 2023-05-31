package com.dky.domain.vo;

import com.dky.domain.dto.MenuDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuVO {
    private List<MenuDto> menus;
    private List<Long> checkedKeys;
}
