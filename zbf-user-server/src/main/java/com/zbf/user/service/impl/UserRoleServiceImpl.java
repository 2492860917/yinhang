package com.zbf.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbf.user.entity.Role;
import com.zbf.user.entity.User;
import com.zbf.user.mapper.UserRoleMapper;
import com.zbf.user.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/18 13:55
 * 描述:
 **/
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public boolean getAdd(User user) {
        return userRoleMapper.getAdd(user);
    }

    @Override
    public List<Role> getRoleList() {
        return userRoleMapper.getRoleList();
    }

    @Override
    public Object getUserList(Map<String, Object> map) {
        return userRoleMapper.getUserList(map);
    }

    @Override
    public void getUserUpdate(User user) {
         userRoleMapper.getUserUpdate(user);
    }

    @Override
    public List<Map<String, Object>> getByRole(Integer id) {
        return userRoleMapper.getByRole(id);
    }

    @Override
    public Boolean Add(User user) {
        return userRoleMapper.add(user);
    }


}
