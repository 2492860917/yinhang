package com.zbf.user.mapper;

import com.zbf.user.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ljl
 * @since 2020-09-12
 */
public interface UserMapper extends BaseMapper<User> {

    @Insert(value = "insert into t_user values(null,#{name},#{sex})")
    public int insertUser(String name,String sex);

    @Select("select * from base_user where loginName=#{loginName}")
    public User getUserByUserloginName(@Param("loginName") String loginName);

    //邮箱的注册
    @Select("select * from base_user where mail=#{mail}")
    User getUserByUsermail(String mail);

//手机号的注册
    @Select("select * from base_user where tel=#{tel}")
    User getUserByUserTel(String tel);

    //注册
    @Insert("insert into base_user(userName,loginName,sex,tel,mail,passWord) values(#{userName},#{loginName},#{sex},#{tel},#{mail},#{passWord})")
    Boolean save(User user);
}
