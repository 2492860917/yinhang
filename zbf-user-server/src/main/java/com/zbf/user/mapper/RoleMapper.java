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


    @Select("select * from base_role")
    List<Map<String, Object>> getRoleAll();

    @Select("select br.name,bm.menuName, GROUP_CONCAT(br.id) roleMenuId from base_role br INNER JOIN base_role_menu brm on br.id=brm.role_id\n" +
            " INNER JOIN base_menu bm on bm.id=brm.menu_id GROUP BY br.id")
    List<Map<String, Object>> getMenuRoleList();
}
