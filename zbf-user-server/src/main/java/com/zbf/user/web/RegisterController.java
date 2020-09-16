package com.zbf.user.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zbf.common.entity.ResponseResult;
import com.zbf.common.exception.AllRedisKeyEnum;
import com.zbf.common.utils.FreemarkerUtils;
import com.zbf.common.utils.JwtUtilsForOther;
import com.zbf.common.utils.MailQQUtils;
import com.zbf.common.utils.UID;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/10  14:58
 * 描述: 这是跟用户注册相关的业务操作类
 */
@RestController
public class RegisterController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 描述: 账户的激活地址
     */
    @Value("${active.path}")
    private String activePath;

    /**
     * 作者: LCG
     * 日期: 2020/9/10  15:13
     * 参数：
     * 返回值：
     * 描述: 注册账户
     */
    @RequestMapping("toRegister")
    public ResponseResult registerUser(@RequestBody Map<String,String> map){

        //1、存储用户的数据

        //2、获取登录名字
        String loginName = map.get("loginName");
        //3、扣扣邮箱发送激活邮件
        MailQQUtils.sendMessage("927613833@qq.com","1234","三斗网络",getActivePath(activePath,1*60*1000L,loginName));

        ResponseResult responseResult = ResponseResult.getResponseResult();

        return responseResult;
    }


    /**
     * 作者: LCG
     * 日期: 2020/9/10  15:13
     * 参数：
     * 返回值：
     * 描述: 激活账户
     */
    @RequestMapping("activeUser")
    public void activeUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //获取激活的串
        String actived = request.getParameter("actived");
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();

        //设置响应头的格式
        response.setContentType("text/html;charset=UTF-8");
        //解析激活串
       try{
           JSONObject jsonObject = JwtUtilsForOther.decodeJwtTocken(actived);

           JSONObject info = JSON.parseObject(jsonObject.getString("info"));

           //获取存储的激活码
           String code = redisTemplate.opsForValue().get(AllRedisKeyEnum.ACTIVIVE_KEY.getKey() + "_" + info.get("loginName"));

           //激活成功后跳转到激活成功页面
           //在激活成功的页面可以跳转到登录界面，进行登录
           //如果激活码正确
           if(info.get("code").equals(code)){

               stringObjectHashMap.put("loginPath","http://localhost:8080/");
               FreemarkerUtils.getStaticHtml(RestController.class,"/template/","activeOk.html",stringObjectHashMap,response.getWriter());
           }else{
               FreemarkerUtils.getStaticHtml(RestController.class,"/template/","activeError.html",stringObjectHashMap,response.getWriter());
           }

       }catch (ExpiredJwtException e){
           HashMap<String, Object> newData = new HashMap<>();
           String loginName = request.getParameter("loginName");
           newData.put("newActiveLink","http://192.168.137.1:10000/user/getNewActiveLink?loginName="+loginName);
           FreemarkerUtils.getStaticHtml(RestController.class,"/template/","activeError.html",newData,response.getWriter());

       }

    }

    /**
     * 作者: LCG
     * 日期: 2020/9/10  15:46
     * 参数：baseActivePath 激活的基本路劲，激活信息,timeOut 有效期
     * 返回值：
     * 描述: 这是一个普通的方法用来生成激活链接
     */
    public String getActivePath(String baseActivePath,long timeOut,String loginName){
        //激活信息
        String code= UID.getUUID16();
        //放入redis 中
        String key=AllRedisKeyEnum.ACTIVIVE_KEY.getKey()+"_"+loginName;
        redisTemplate.opsForValue().set(key,code);
        //设置redis的key过期时间
        redisTemplate.expire(key,timeOut,TimeUnit.MILLISECONDS);
        //生成激活的链接地址
        StringBuffer stringBuffer=new StringBuffer(activePath);
        stringBuffer.append("?");
        stringBuffer.append("loginName="+loginName);
        stringBuffer.append("&");
        stringBuffer.append("actived=");
        Map<String,String> map=new HashMap<>();
        map.put("loginName",loginName);
        map.put("code",code);
        stringBuffer.append(JwtUtilsForOther.generateToken(JSON.toJSONString(map),timeOut));
        String path=stringBuffer.toString();
        stringBuffer=null;
        return path;
    }

    
    /**
     * 作者: LCG
     * 日期: 2020/9/14  9:24
     * 描述: 激活失败重新获取激活链接邮件
     * @Param [request, response]
     * @Return void
     */
    @RequestMapping("getNewActiveLink")
    public void getNewActiveLink(HttpServletRequest request,HttpServletResponse response) throws Exception {

        HashMap<String, Object> stringObjectHashMap = new HashMap<>();

        //设置响应头的格式
        response.setContentType("text/html;charset=UTF-8");

        //如果jwt过期，重新的发激活邮件
        String loginName = request.getParameter("loginName");
        //根据loginName获取用户信息

        //3、扣扣邮箱发送激活邮件
        MailQQUtils.sendMessage("927613833@qq.com","1234","三斗网络",getActivePath(activePath,1*60*1000L,loginName));

        FreemarkerUtils.getStaticHtml(RestController.class,"/template/","sendOK.html",stringObjectHashMap,response.getWriter());
    }

}
