package com.zbf.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbf.common.entity.ResponseResult;
import com.zbf.common.exception.AllStatusEnum;
import com.zbf.user.entity.Role;
import com.zbf.user.entity.User;
import com.zbf.user.service.UserRoleService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
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
    //word导出
    @RequestMapping("getDoc")
    public boolean getDoc(@RequestBody User user) throws IOException, TemplateException {
        System.out.println("user:" + user);
        try {
            Map<String, String> dataMap = new HashMap<String, String>();
            if (user.getId() != null) {
                dataMap.put("id", String.valueOf(user.getId()));
            } else {
                dataMap.put("id", "该用户没有id");
            }
            if (user.getUserName() != null) {
                dataMap.put("userName", user.getUserName());
            } else {
                dataMap.put("userName", "该用户没有姓名");
            }
            if (user.getLoginName() != null) {
                dataMap.put("loginName", user.getLoginName());
            } else {
                dataMap.put("loginName", "该用户没有登录姓名");
            }
            if (user.getSex() != null) {
                dataMap.put("sex", user.getSex());
            } else {
                dataMap.put("sex", "性别保密");
            }
            if (user.getMail() != null) {
                dataMap.put("mail", user.getMail());
            } else {
                dataMap.put("mail", "该用户没有邮箱");
            }
            if (user.getTel() != null) {
                dataMap.put("tel", user.getTel());
            } else {
                dataMap.put("tel", "该用户没有手机号");
            }
           /* if (user.getSalt() != null) {
                String s = ImageToBase64ByOnline(user.getSalt());
                s.substring(s.indexOf(',') + 1);

                dataMap.put("salt", s);
            } else {
                dataMap.put("salt", "该用户没有头像");
            }*/


            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("utf-8");
            //指定模板路径的第二种方式,我的路径是D:/      还有其他方式
            configuration.setDirectoryForTemplateLoading(new File("D:/Doc"));


            // 输出文档路径及名称
            File outFile = new File("D:/Doc/" + user.getUserName() + ".doc");
            //以utf-8的编码读取ftl文件
            Template t = configuration.getTemplate("jian7.ftl", "utf-8");
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);
            t.process(dataMap, out);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String ImageToBase64ByOnline(String imgURL) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgURL);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data.toByteArray());
    }


}
