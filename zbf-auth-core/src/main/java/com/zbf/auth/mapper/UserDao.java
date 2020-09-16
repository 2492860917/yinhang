package com.zbf.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 作者: LCG
 * 日期: 2020/8/26 20:28
 * 描述:
 */
@Mapper
public interface UserDao {

    @Select("select * from base_user where loginName=#{userName}")
    public Map<String, Object> getUserByUserName(String userName);


    /**
     * 作者: LCG
     * 日期: 2020/9/7  23:04
     * 参数：
     * 返回值：
     * 描述: 获取当前user的所有的角色
     */
    @Select("select UPPER(br.role_code) roleCode from base_role br INNER JOIN base_user_role bur ON br.id=bur.roleId where bur.userId=#{userId}")
    public List<String> getUserRole(Long userId);
    @Select("select * from base_user where tel=#{s}")
    Map<String, Object> getUserByTel(String s);
    @Select("select * from base_user where email=#{s}")
    Map<String, Object> getUserByemail(String s);
}