package com.zbf.user.mapper;

import com.zbf.user.entity.Menu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
@Repository
public interface MenuMapper {


    //全部一级菜单
    @Select("SELECT DISTINCT bu.id userId,bu.loginName,bm.code,bm.id menuId,bm.menuName,bm.url,bm.leval,bm.imagePath,bm.parentCode,bm.version,bm.createTime FROM base_user_role bur INNER JOIN base_user bu ON bu.id = bur.userId INNER JOIN base_role_menu brm ON brm.role_id = bur.roleId INNER JOIN base_menu bm on bm.id = brm.menu_id WHERE bu.loginName = 'zhangsan1' AND bm.leval = 1")
    List<Map<String, Object>> getMenulist(String loginName);


    //全部菜单
    @Select(value = "SELECT DISTINCT bu.id userId,bu.loginName,bm.code,bm.id menuId,bm.menuName,bm.url,bm.leval,bm.imagePath,bm.parentCode,bm.version,bm.createTime FROM\n" +
            "base_user_role bur\n" +
            "INNER JOIN base_user bu ON bu.id = bur.userId\n" +
            "INNER JOIN base_role_menu brm ON brm.role_id = bur.roleId\n" +
            "INNER JOIN base_menu bm ON bm.id = brm.menu_id \n" +
            "WHERE bu.loginName='zhangsan1' and bm.leval=#{leval} and parentCode=#{parentCode}")
    List<Map<String, Object>> getUserMenu(Map<String, Object> map);

    //添加菜单
    @Insert("insert into base_menu values id=#{id},version=#{version},code=#{code},menuName=#{menuName},imagePath=#{imagePath},url=#{url},parentCode=#{parentCode},leval=#{leval},createTime=#{createTime}")
    void save(Menu menu);

    //删除菜单
    @Delete("delete from  base_menu where id=#{id}")
    void del(BigInteger id);


    //修改菜单
    @Update("update base_menu set menuName=#{menuName},imagePath=#{imagePath},url=#{url} where id=#{id}")
    boolean update(Menu menu);

    //菜单列表
    @Select("select * from base_menu where leval=1")
    List<Map<String, Object>> getAllMenu();

    //菜单二级
    @Select("select * from base_menu where leval=#{leval} and parentCode=#{parentCode}")
    List<Map<String, Object>> getddMenu(Map<String, Object> dd);
}
