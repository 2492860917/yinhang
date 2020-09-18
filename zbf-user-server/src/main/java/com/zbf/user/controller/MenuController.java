package com.zbf.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zbf.common.entity.ResponseResult;
import com.zbf.common.exception.AllRedisKeyEnum;
import com.zbf.common.exception.AllStatusEnum;
import com.zbf.user.entity.Menu;
import com.zbf.user.service.impl.MenuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/16 15:10
 * 描述:
 **/
@RestController
public class MenuController {
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    private MenuServiceImpl menuService;

    /**
     * 权限列表
     * @return
     */
    @RequestMapping("/getAuthAll")
    public ResponseResult getAuthAll(){
        ResponseResult responseResult=new ResponseResult();
        List<Map<String, Object>> menuAll = menuService.getAll();
        responseResult.setCode(AllStatusEnum.REQUEST_SUCCESS.getCode());
        responseResult.setSuccess(AllStatusEnum.REQUEST_SUCCESS.getMsg());
         responseResult.setResult(menuAll);
         return  responseResult;

    }

    //权限列表三级
    @RequestMapping("/getAuthMenu")
    public ResponseResult menuLists(String loginName){

        ResponseResult responseResult=new ResponseResult();
        String authMenuId = AllRedisKeyEnum.USER_MENU_KEY.getKey() +loginName;
        String menusList = redisTemplate.opsForValue().get(authMenuId);
        if (menusList==null||menusList.equals("")){
            List<Map<String, Object>> menulist = menuService.getMenulist(loginName);
            redisTemplate.opsForValue().set(authMenuId, JSON.toJSONString(menulist));
            responseResult.setCode(AllStatusEnum.REQUEST_SUCCESS.getCode());
            responseResult.setSuccess(AllStatusEnum.REQUEST_SUCCESS.getMsg());
            responseResult.setResult(menulist);
        }else {
            JSONArray jsonArry = JSON.parseArray(menusList);
            responseResult.setCode(AllStatusEnum.REQUEST_SUCCESS.getCode());
            responseResult.setSuccess(AllStatusEnum.REQUEST_SUCCESS.getMsg());
            responseResult.setResult(jsonArry);
        }
        return responseResult;


    }
    //权限添加
    @RequestMapping("getAddMenu")
    public boolean add(@RequestBody Menu menu){
         menuService.save(menu);
        return true;
    }
    //权限删除
    @RequestMapping("getdelMenu")
    public boolean delete(BigInteger id){
        return menuService.del(id);
    }
    //权限修改
    @RequestMapping("getUpMenu")
    public boolean update(Menu menu){
        return menuService.update(menu);
    }
}
