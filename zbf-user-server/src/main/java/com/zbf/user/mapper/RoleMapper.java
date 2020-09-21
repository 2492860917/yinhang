package com.zbf.user.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/20 20:39
 * 描述:
 **/
public interface RoleMapper {

    @Select("select * from base_role where name like concat('%',#{roleName},'%')")
    List<Map<String,Object>> getRoleList(String roleName);
    @Insert("insert into base_user_role(roleId,userId) values(#{roleId},#{userId})")
    public Boolean getUserRole(Long userId,long roleId);
}
