package com.zbf.user.mapper;

import com.zbf.user.entity.User;

import java.util.List;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/18 13:59
 * 描述:
 **/
public interface UserRoleMapper {

    List<User> selects(User user);
}
