package com.zbf.user.service;

import com.zbf.user.entity.User;

import java.util.List;

/**
 * 用户接口
 */
public interface UserRoleService {
    List<User> selects(User user);
}
