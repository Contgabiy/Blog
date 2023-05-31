package com.dky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dky.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-16 10:29:57
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectAllRoutersMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}

