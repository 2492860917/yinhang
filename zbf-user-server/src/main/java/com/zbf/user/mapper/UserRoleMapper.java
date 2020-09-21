package com.zbf.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbf.user.entity.Role;
import com.zbf.user.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/18 13:59
 * 描述:
 **/
public interface UserRoleMapper {

    //用户数据
    IPage<User> selects(Page page, User uservo);
    

    //用户添加
    @Insert("insert into base_user values(id=#{id},version=#{version},userName=#{userName},loginName=#{loginName},passWord=#{passWord},buMen=#{buMen},salt=#{salt},createTime=#{createTime}")
    boolean getAdd(User user);


    @Select("select * from base_role")
    List<Role> getRoleList();


    @Select("<script> SELECT * from base_user" +
            "<where>" +
            "<if test='userName != null and userName !=\"\"'>and userName like CONCAT('%',#{userName},'%')</if> " +
            "<if test='loginName != null and loginName !=\"\"'>and loginName like CONCAT('%',#{loginName},'%') </if>" +
            "<if test='tel != null and tel !=\"\"'>and tel like CONCAT('%',#{tel},'%') </if>" +
            "</where>" +
            "</script>"
    )
    List<Map<String, Object>> getUserList(Map<String, Object> map);

    @Select(value = "update base_user set version=#{version} where id=#{id}")
     public void getUserUpdate(User user);

    @Select("select bu.*,GROUP_CONCAT(br.`name`) rname,GROUP_CONCAT(br.id) rid from base_user bu " +
            "INNER JOIN base_user_role bur on bu.id=bur.userId INNER JOIN base_role br on bur.roleId=br.id " +
            "where bu.id=#{id} GROUP BY bu.id")
    List<Map<String, Object>> getByRole(Integer id);

    @Insert("insert into base_user(userName,loginName,tel,mail,passWord,salt) values(#{userName},#{loginName},#{tel},#{mail},#{passWord},#{salt})")
    Boolean add(User user);
    @Select("select * from base_user_role where userId=#{userId}")
    List<Map<String, Object>> getBur(Long userId);
}