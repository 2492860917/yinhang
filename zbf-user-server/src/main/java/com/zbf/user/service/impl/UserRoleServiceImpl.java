package com.zbf.user.service.impl;

import com.zbf.user.entity.User;
import com.zbf.user.mapper.UserRoleMapper;
import com.zbf.user.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<User> selects(User user) {
        return userRoleMapper.selects(user);
    }
}
