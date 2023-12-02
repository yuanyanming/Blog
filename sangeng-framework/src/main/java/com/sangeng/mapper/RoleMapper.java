package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-13 07:36:36
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRolesByUser(Long userId);
}
