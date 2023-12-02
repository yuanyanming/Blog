package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-13 07:36:18
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> getPermissionsByUser(Long userId);

    List<Menu> getRouters(Long userId);
}
