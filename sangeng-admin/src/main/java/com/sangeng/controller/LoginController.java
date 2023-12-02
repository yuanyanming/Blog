package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.User;
import com.sangeng.domain.vo.MenuVo;
import com.sangeng.domain.vo.PermissionInfoVo;
import com.sangeng.domain.vo.RouterVo;
import com.sangeng.domain.vo.UserInfoVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.mapper.MenuMapper;
import com.sangeng.mapper.RoleMapper;
import com.sangeng.service.LoginService;
import com.sangeng.service.MenuService;
import com.sangeng.service.RoleService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<PermissionInfoVo> getInfo(){
        //获得用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id获得permission
        //permission在menu表里，需要根据userid->roleid->menuid
        List<String> permissions=menuService.getPermissionsByUser(userId);
        //获取roles
        List<String> roles= roleService.selectRolesByUser(userId);
        //获取user
        User user = SecurityUtils.getLoginUser().getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        PermissionInfoVo permissionInfoVo = new PermissionInfoVo(permissions,roles,userInfoVo);
        return ResponseResult.okResult(permissionInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult<RouterVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu表单
        List<MenuVo> routers=menuService.getRouters(userId);
        return ResponseResult.okResult(new RouterVo(routers));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }

}