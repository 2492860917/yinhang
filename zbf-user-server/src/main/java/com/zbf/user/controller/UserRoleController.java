package com.zbf.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbf.common.entity.ResponseResult;
import com.zbf.common.exception.AllStatusEnum;
import com.zbf.user.entity.Role;
import com.zbf.user.entity.User;
import com.zbf.user.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
     * @param
     * @return
     */
    @RequestMapping("getUserList")
    public ResponseResult getUserList(@RequestBody Map<String, Object> map){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setResult(userRoleService.getUserList(map));
        return responseResult;
    }

    @RequestMapping("getUserUpdate")
    public void getUserUpdate(@RequestBody User user){
        if (user.getVersion() ==1){
            user.setVersion(0);
        }else {
            user.setVersion(1);
        }
        userRoleService.getUserUpdate(user);
    }

    @RequestMapping("getAddUser")
    public boolean getAddUser(@RequestBody User user){

        return  userRoleService.getAdd(user);
    }
    //dd
    @RequestMapping("getRolesList")
    public ResponseResult getRoleList(){
        ResponseResult responseResult=new ResponseResult();
        List<Role> selects = userRoleService.getRoleList();
        responseResult.setCode(AllStatusEnum.REQUEST_SUCCESS.getCode());
        responseResult.setSuccess(AllStatusEnum.REQUEST_SUCCESS.getMsg());
        responseResult.setResult(selects);
        return  responseResult;
    }


    //查询用户的角色
    @RequestMapping("getByRole")
    public  ResponseResult getByRole(@RequestParam("id")Integer id){
        ResponseResult responseResult=new ResponseResult();
        List<Map<String,Object>> getByRole=userRoleService.getByRole(id);
        responseResult.setResult(getByRole);
        return responseResult;
    }
    @RequestMapping("upload")
    public String upload(MultipartFile file) {
        System.err.println(file);
        try {

            if (file!=null){
                if (!file.isEmpty()){
                    String originalFilename = file.getOriginalFilename();
                    String fileName = UUID.randomUUID() + "_" + originalFilename;
                    File file1=new File("D://pic//",fileName);
                    if (file1.getParentFile().exists()){
                        file1.getParentFile().mkdirs();
                    }
                    file.transferTo(file1);
                    return "http://localhost:10002/pic/"+fileName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //添加
    @RequestMapping("Add")
    public ResponseResult add(@RequestBody User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(user.getPassWord());
        user.setPassWord(encode);
        ResponseResult responseResult = new ResponseResult();
        Boolean add = userRoleService.Add(user);
        if (add) {
            responseResult.setSuccess("成功");
            return responseResult;
        } else {
            responseResult.setSuccess("失败");
            return responseResult;
        }

    }

}
