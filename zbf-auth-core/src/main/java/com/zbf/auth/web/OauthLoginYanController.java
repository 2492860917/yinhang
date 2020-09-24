package com.zbf.auth.web;

import com.zbf.auth.mapper.SmsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/23 19:48
 * 描述:
 **/
@RestController
public class OauthLoginYanController {
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    SmsDao smsDao;

    @RequestMapping("smsyzm")
    public boolean smsyzm(String phone){
        System.err.println(phone+"++++++++++++++++");
        boolean smys = smsDao.smys(phone);
        return smys;
    }

    @RequestMapping("toNext")
    public boolean toNext(@RequestParam("code")String code){
        String codes = redisTemplate.opsForValue().get("codes");
        if (code.equals(codes)){
            return true;
        }else{
            return false;
        }
    }
}
