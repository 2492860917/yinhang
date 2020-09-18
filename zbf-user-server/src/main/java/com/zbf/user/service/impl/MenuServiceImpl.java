package com.zbf.user.service.impl;

import com.zbf.user.entity.Menu;
import com.zbf.user.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/16 15:09
 * 描述:
 **/
@Service
public class MenuServiceImpl {

    @Autowired
    private MenuMapper menuMapper;

    /**
     * Menu一级菜单
     * @param loginName
     * @return
     */
    public List<Map<String, Object>> getMenulist(String loginName) {
        List<Map<String,Object>> list1=menuMapper.getMenulist(loginName);
        this.getMenu(list1,loginName);
        return list1;
    }

    /**
     * Menue二级，三级菜单
     * @param loginName
     * @return
     */
    public void getMenu(List<Map<String, Object>> list, String loginName){
        for (Map<String,Object> menu:list){
            Map<String,Object> dd=new HashMap<>();
            dd.put("loginName",loginName);
            dd.put("leval",Integer.valueOf(menu.get("leval").toString())+1);
            dd.put("parentCode",menu.get("code"));
            List<Map<String,Object>> userMenu= menuMapper.getUserMenu(dd);
            if (userMenu.size()>0){
                menu.put("listMenu",userMenu);
                this.getMenu(userMenu,loginName);
            }else {
                userMenu=new ArrayList<>();
                menu.put("listMenu",userMenu);
                break;
            }
        }

    }


    /**
     * 添加
     * @param menu
     * @return
     */
    public boolean save(Menu menu) {
         menuMapper.save(menu);
        return true;
    }

    //权限删除
    public boolean del(BigInteger id) {
        menuMapper.del(id);
        return true;
    }



    //权限修改
    public boolean update(Menu menu) {
        return  menuMapper.update(menu);
    }

    /**
     * 权限列表
     * @return
     */
    public List<Map<String, Object>> getAll() {
        List<Map<String,Object>> list1=menuMapper.getAllMenu();
        this.getMenuAll(list1);
        return list1;
    }

    //权限菜单
    private void getMenuAll(List<Map<String, Object>> list) {
        for (Map<String,Object> menu:list){
            Map<String,Object> dd=new HashMap<>();
            dd.put("leval",Integer.valueOf(menu.get("leval").toString())+1);
            dd.put("parentCode",menu.get("code"));
            List<Map<String,Object>> userMenu= menuMapper.getddMenu(dd);
            if (userMenu.size()>0){
                menu.put("listMenu",userMenu);
                this.getMenuAll(userMenu);
            }else {
                userMenu=new ArrayList<>();
                menu.put("listMenu",userMenu);
                break;
            }
        }
    }

}
