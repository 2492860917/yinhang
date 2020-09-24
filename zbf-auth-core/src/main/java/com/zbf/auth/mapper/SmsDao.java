package com.zbf.auth.mapper;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/23 19:37
 * 描述:
 **/
@FeignClient(name="feign11")
public interface SmsDao {
    @RequestMapping("sms/smsy")
    boolean smys(@RequestParam("phone") String phone);
}
