package com.ucpaas.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.zbf.common.utils.MailQQUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Random;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/23 19:41
 * 描述:
 **/

@RestController
@RequestMapping("sms")
public class SmsController {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @RequestMapping("smsy")
    public boolean smsy(@RequestParam("phone") String phone) {
        if (phone.contains("@")) {
            System.err.println("邮箱+++++++++++++++++++++++++++++++++++");
            Random random = new Random();
            int rd = random.nextInt(9999);
            String rr = "" + rd;
            redisTemplate.opsForValue().set("codes", rr);
            try {
                MailQQUtils.sendMessage(phone,rr,"ddd","");
                return true;
            }catch (Exception ex){
                return false;
            }

        } else {


            DefaultProfile profile = DefaultProfile.getProfile("1",
                    "LTAI4GCxcdGFY3G91qTP58MU",
                    "yZDvHIUTIyZe4QLSwAYv2XUDVWGiK1");
            IAcsClient client = new DefaultAcsClient(profile);

            //构建请求，默认即可
            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");   //不要动
            request.setVersion("2017-05-25");             //不要动
            request.setAction("SendSms");

            //自定义的参数（手机号，验证码，签名，模板）
            request.putQueryParameter("PhoneNumbers", phone);//短信服务的手机号
            request.putQueryParameter("SignName", "小小果实");//短信服务中「签名名称」
            request.putQueryParameter("TemplateCode", "SMS_202355464"); //短息服务中「模版CODE」
            //构建一个短信的验证码
            HashMap<String, Object> map = new HashMap<>();
            Random random = new Random();
            int rd = random.nextInt(9999);
            String rr = "" + rd;
            redisTemplate.opsForValue().set("codes", rr);
            map.put("code", rr);
            request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));//JSONObject使用前面Maven导入到fastjson依赖
            try {
                CommonResponse response = client.getCommonResponse(request);
                //是否发送成功
                System.out.println(response.getData());
                if (response.getHttpStatus() == 200) {
                    return true;
                } else {
                    return false;
                }

            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
    @RequestMapping("activation")
    public boolean activation(@RequestParam("mail") String mail) {
        System.err.println("邮箱激活+++++++++++++++++++++++++++++++++++"+mail);
         /*   Random random = new Random();
        int rd = random.nextInt(9999);
        String rr = "" + rd;
        redisTemplate.opsForValue().set("codes", rr);*/
        try {
            System.out.println("-------------------");
            MailQQUtils.sendMessage(mail,"","ddd","www.baidu.com");
            System.out.println("--------------------+");
            return true;
        }catch (Exception ex){
            return false;
        }
    }

}
