package com.zbf.user.controller;

import com.zbf.common.entity.ResponseResult;
import com.zbf.common.exception.AllStatusEnum;
import com.zbf.user.entity.User;
import com.zbf.user.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/18 13:54
 * 描述:
 **/

@RestController
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 用户列表
     * @param user
     * @return
     */
    @RequestMapping("getUserList")
    public ResponseResult getUserList(User user){
        ResponseResult responseResult=new ResponseResult();
        List<User> selects = userRoleService.selects(user);
        responseResult.setCode(AllStatusEnum.REQUEST_SUCCESS.getCode());
        responseResult.setSuccess(AllStatusEnum.REQUEST_SUCCESS.getMsg());
        responseResult.setResult(selects);
        return  responseResult;
    }

}
