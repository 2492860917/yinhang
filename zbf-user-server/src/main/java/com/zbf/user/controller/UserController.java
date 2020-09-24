package com.zbf.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zbf.common.entity.ResponseResult;
import com.zbf.common.entity.ResponseResultEnum;
import com.zbf.common.utils.ReadExcel;
import com.zbf.user.entity.User;
import com.zbf.user.mapper.UserMapper;
import com.zbf.user.service.IUserService;
import io.netty.util.internal.SocketUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Ljl
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
        return userService.updateById(user);
    }


    /**
     * 解析Excel
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "toImportExcel",method = RequestMethod.POST)
    public ResponseResult toImportExcel(MultipartFile file) throws IOException {

        //获取文件的名字
        String name = file.getName();

        //获取文件的输入流
        InputStream inputStream = file.getInputStream();

        XSSFWorkbook  xssfWorkbook=new XSSFWorkbook(inputStream);

        Iterator<Sheet> iterator = xssfWorkbook.iterator();

        while (iterator.hasNext()){

            Sheet next = iterator.next();

            int physicalNumberOfRows = next.getPhysicalNumberOfRows();

            for(int u=0;u<physicalNumberOfRows;u++){

                System.out.println("=====>"+next.getRow(u).getCell(0).getNumericCellValue());

            }

        }

        ResponseResult responseResult = ResponseResult.getResponseResult();

        return responseResult;
    }

    @Autowired
    private UserMapper userMapper;


    @RequestMapping("ExcelImport")
    public String readExcelFile(MultipartFile file) {
        String result = "";
        //创建处理EXCEL的类
        ReadExcel readExcel = new ReadExcel();
        //解析excel，获取上传的事件单
        List<Map<String, Object>> userList = readExcel.getExcelInfo(file);
        //至此已经将excel中的数据转换到list里面了,接下来就可以操作list,可以进行保存到数据库,或者其他操作,
        for(Map<String, Object> user:userList){
            int ret = userMapper.insertUser(user.get("name").toString(), user.get("sex").toString());
            if(ret == 0){
                result = "插入数据库失败";
            }
        }
        if(userList != null && !userList.isEmpty()){
            result = "上传成功";
        }else{
            result = "上传失败";
        }
        return result;
    }

    @RequestMapping("getName")
    public ResponseResult getName(@RequestParam("loginName") String loginName){
        User userByUserloginName = userMapper.getUserByUserloginName(loginName);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setResult(userByUserloginName);
        return responseResult;
    }




}

