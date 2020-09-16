package com.zbf.user.web;

import com.zbf.common.utils.MailQQUtils;
import com.zbf.common.utils.RanDomUtils;
import com.zbf.user.mapper.LoginPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

/**
 * Ljl
 */
@RestController
public class YzmRedis {

    private static final Pattern PATTERN_PHONE = Pattern.compile("^-?\\d+(\\.\\d+)?$");
    @Autowired
    private LoginPhone loginPhone;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @RequestMapping("/getyzm")
    public boolean getyzm(@RequestParam("tel") String tel){
        if(isPhone(tel)){
            return loginPhone.dd(tel);
        }else{
            String fourRandom = RanDomUtils.getFourRandom();
            MailQQUtils.sendMessage(tel,fourRandom,"李氏科技","");
            return  true;
        }

    }
    public boolean isPhone(String phone){
        return PATTERN_PHONE.matcher(phone).matches();
    }
}
