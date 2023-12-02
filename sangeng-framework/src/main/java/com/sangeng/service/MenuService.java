package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.vo.MenuVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-10-13 07:36:19
 */
public interface MenuService extends IService<Menu> {

    List<String> getPermissionsByUser(Long userId);

    List<MenuVo> getRouters(Long userId);
}
