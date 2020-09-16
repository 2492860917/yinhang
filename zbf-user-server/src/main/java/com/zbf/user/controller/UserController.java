package com.zbf.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zbf.common.entity.ResponseResult;
import com.zbf.common.entity.ResponseResultEnum;
import com.zbf.user.entity.User;
import com.zbf.user.service.IUserService;
import io.netty.util.internal.SocketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.soap.SOAPBinding;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ssf
 * @since 2020-09-12
 */
@RestController
public class UserController {
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    IUserService userService;
    @RequestMapping("test01")
    public String test01(){
        return "ok";
    }
    @RequestMapping("/register1")
    public boolean register1(String password){
        String yzm = redisTemplate.opsForValue().get("yzm");
        System.err.println(yzm+"---------------------------------------------------------------");
        if(password.equals(yzm)){
            ResponseResult.getResponseResult().setCode(1006);
            return true;
        }
        return false;
    }
    @RequestMapping("/Register")
    public boolean Register(@RequestBody User user){
        user.setCreateTime(new Date());
        boolean save = userService.save(user);
        if(save){
            ResponseResult.getResponseResult().setCode(1006);
            return true;
        }
        return false;
    }
    @RequestMapping("Updatepassword")
    public boolean Updatepassword(@RequestBody User user){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("tel",user.getTel());
        User one = userService.getOne(userQueryWrapper);
        user.setId(one.getId());
        user.setUpdateTime(new Date());
        return userService.updateById(user);
    }

}

