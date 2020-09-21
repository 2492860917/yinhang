package com.zbf.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbf.user.entity.Role;
import com.zbf.user.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户接口
 */
public interface UserRoleService {



    boolean getAdd(User user);

    List<Role> getRoleList();


    Object getUserList(Map<String, Object> map);

    void getUserUpdate(User user);

    List<Map<String, Object>> getByRole(Integer id);

    Boolean Add(User user);
}
