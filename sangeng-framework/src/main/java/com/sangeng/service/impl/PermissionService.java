package com.sangeng.service.impl;

import com.sangeng.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {

    /**
     * 判断当前用户是否有permission
     * @param permission 权限
     * @return
     */
    public boolean hasPermission(String permission){
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
