package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-10-13 07:36:37
 */
public interface RoleService extends IService<Role> {

    List<String> selectRolesByUser(Long userId);
}
