package com.zbf.user.controller;

import com.zbf.common.entity.ResponseResult;
import com.zbf.common.utils.MailQQUtils;
import com.zbf.user.entity.User;
import com.zbf.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/23 15:26
 * 描述:
 **/

@RestController
@RequestMapping("auth")
public class UseregisController {
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserMapper userMapper;


    /**
     * 描述跳转的地址
     */
    @Value("${active.path}")
    private String activePath;

    //注册

    @RequestMapping("toRegister")
    public ResponseResult toRegister(@RequestBody User user){

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(user.getPassWord());
        user.setPassWord(encode);

        ResponseResult responseResult = new ResponseResult();
        String regex = "[\u4e00-\u9fa5]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(user.getLoginName());
        boolean b=matcher.find();
        if (b){
            responseResult.setCode(3);
            responseResult.setError("登录名不能使用中文");
            return responseResult;
        }
        User userByUserloginName = userMapper.getUserByUserloginName(user.getLoginName());

        if (userByUserloginName != null) {
            responseResult.setCode(0);
            responseResult.setError("登录名已被人使用");
            return responseResult;
        }
        User userByUsermail = userMapper.getUserByUsermail(user.getMail());

        if (userByUsermail != null) {
            responseResult.setCode(1);
            responseResult.setError("该邮箱已经注册过了");
            return responseResult;
        }
        User userByUserTel = userMapper.getUserByUserTel(user.getTel());
        if (userByUserTel != null) {
            responseResult.setCode(2);
            responseResult.setError("该手机号已经注册过了");
            return responseResult;
        }
        MailQQUtils.sendMessage("2492860917@qq.com","dddd","6666","");
        Boolean save = userMapper.save(user);
        if (save) {
            responseResult.setCode(6);
            responseResult.setSuccess("注册成功");
            return responseResult;
        } else {
            return responseResult;
        }

    }

}
