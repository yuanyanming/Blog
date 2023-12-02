package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.vo.MenuVo;
import com.sangeng.mapper.MenuMapper;
import com.sangeng.service.MenuService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-10-13 07:36:20
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> getPermissionsByUser(Long userId) {
        MenuMapper baseMapper = getBaseMapper();
        List<String> perms=null;
        //如果userid是1，选择全部permissions
        if(userId.equals(1L)){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            queryWrapper.in(Menu::getMenuType,SystemConstants.MENU,SystemConstants.BUTTON);
            List<Menu> list = list(queryWrapper);
            perms=list.stream()
                .map(Menu::getPerms)
                .collect(Collectors.toList());
        }else{
            perms = baseMapper.getPermissionsByUser(userId);
        }
        //否则按照数据库选择
        return perms;
    }

    @Override
    public List<MenuVo> getRouters(Long userId) {
        List<Menu> list=null;
        //如果是admin，选择全部routers
        if(SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType,SystemConstants.DIRECTORY,SystemConstants.MENU);
            queryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
            list = list(queryWrapper);
        }else{
            list=getBaseMapper().getRouters(userId);
        }
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(list, MenuVo.class);
        //构建tree，找出第一层
        List<MenuVo> menuVos1 = buildMenuTree(menuVos, 0L);
        return menuVos1;
    }

    private List<MenuVo> buildMenuTree(List<MenuVo> menus,Long parentId){
        List<MenuVo> menuTree = menus.stream()
                .filter(menuVo -> parentId.equals(menuVo.getParentId()))
                .map(menuVo -> menuVo.setChildren(buildMenuTree(menus, menuVo.getId())))
                .collect(Collectors.toList());
        return menuTree;
    }

}
