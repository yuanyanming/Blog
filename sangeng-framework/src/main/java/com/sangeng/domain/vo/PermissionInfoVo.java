package com.sangeng.domain.vo;

import com.sangeng.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionInfoVo {

    private List<String> permissions;

    private List<String> roles;

    private UserInfoVo user;

}
