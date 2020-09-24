package com.zbf.user.controller;

import com.zbf.common.entity.ExcleEntity;
import com.zbf.common.entity.ResponseResult;
import com.zbf.common.exception.AllStatusEnum;
import com.zbf.common.utils.PoiUtil;
import com.zbf.user.mapper.RoleMapper;
import com.zbf.user.mapper.UserRoleMapper;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/20 20:01
 * 描述:
 **/
@RestController
public class RoleController {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userMapper;

    @RequestMapping("getRoleList")
    public ResponseResult getRoleList(@RequestParam("roleName") String roleName) {
        ResponseResult responseResult = new ResponseResult();
        List<Map<String, Object>> roleList = roleMapper.getRoleList(roleName);
        responseResult.setResult(roleList);
        return responseResult;
    }

    /*
     * @Author ljl
     * @Date 2020/9/20 9:04
     *描述：绑定角色
     * @Param [userId, roleIds]
     * @return com.zbf.common.entity.ResponseResult
     */
    @RequestMapping("bindRoleForUser")
    public ResponseResult getUserRole(Long userId, Integer[] roleIds) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setResult(AllStatusEnum.REQUEST_SUCCESS);
        responseResult.setResult(AllStatusEnum.REQUEST_FAIRLE);
        List<Map<String, Object>> bur = userMapper.getBur(userId);
        String ids = "";
        for (Map<String, Object> ur : bur) {
            ids += ur.get("roleId");
        }
        for (Integer roleId : roleIds) {
            if (!ids.contains("" + roleId)) {
                roleMapper.getUserRole(userId, roleId);
            }
        }
        return responseResult;
    }

    //excel导出
    @RequestMapping("toExcel")
    public ResponseResult toExcel(@RequestBody ExcleEntity excleEntity){
        System.err.println(excleEntity);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        try {
            //通过数据源获取与数据库的连接  没配置spring的可用原生的jdbc来获取连接
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/transaction?serverTimezone=UTC", "root", "root");
            //查询的sql语句

            //拼接需要查询的字段
            StringBuffer stringBufferSelect = new StringBuffer();
            String types = "";
            if(excleEntity.getType()!=null && excleEntity.getType().length>0){
                types = "";
                for (String type:excleEntity.getType()) {
                    types += type+",";
                }
                stringBufferSelect.append(types.substring(0,types.length()-1));
            }else{
                stringBufferSelect.append("u.*");
            }

            //拼接需要根据什么排序
            StringBuffer stringBufferOrderType = new StringBuffer();
            String orders = "";
            if(excleEntity.getOrderType()!=null && excleEntity.getOrderType().length>0){
                stringBufferOrderType.append("ORDER BY ");
                orders = "";
                if(excleEntity.getOrderType().length>1){
                    for (String order:excleEntity.getOrderType()) {
                        orders += order+" and ";
                    }
                    stringBufferOrderType.append(orders.substring(0,orders.length()-4));
                }else{
                    String[] orderType = excleEntity.getOrderType();
                    stringBufferOrderType.append(orderType[0]);
                }
            }else{
                stringBufferOrderType.append(" ");
            }

            //拼接需要根据什么排序
            StringBuffer stringBufferOrder = new StringBuffer();
            //拼接需要升序还是降序
            if(excleEntity.getOrder()!=null && !("").equals(excleEntity.getOrder())){
                stringBufferOrder.append(" "+excleEntity.getOrder());
            }else{
                stringBufferOrder.append(" ");
            }

            //拼接要导出多少条数据
            StringBuffer stringBufferLimit = new StringBuffer();
            if(excleEntity.getTotal()!=null && excleEntity.getTotal()!=0){
                stringBufferLimit.append(" LIMIT 0,"+excleEntity.getTotal());
            }else{
                stringBufferOrder.append(" ");
            }

            StringBuilder sql=new StringBuilder("select "+stringBufferSelect+" from base_user u LEFT JOIN base_user_role ur on u.id = ur.userId" +
                    " LEFT JOIN base_role r on r.id = ur.roleId GROUP BY u.id "+stringBufferOrderType+stringBufferOrder+stringBufferLimit);

            System.err.println("打印sql=="+sql);
            //生成的excel表的路径   注意文件名要和数据库中表的名称一致  因为导入时要用到。
            String filePath="D:\\Excel\\"+excleEntity.getExcleName()+".xls";
            //导出
            PoiUtil.exportToExcel(connection,sql.toString(), "base_user", filePath);
            responseResult.setCode(200);
            responseResult.setSuccess("导出excel成功");
            return responseResult;
        }catch (Exception x){
            x.printStackTrace();
            responseResult.setCode(500);
            responseResult.setSuccess("导出excel失败");
            return responseResult;
        }

    }

    @RequestMapping("getRoleAll")
    public ResponseResult getRoleAll(){
        ResponseResult responseResult = new ResponseResult();
         List<Map<String,Object>>  roleAll= roleMapper.getRoleAll();
         responseResult.setResult(roleAll);
         return  responseResult;
    }


    @RequestMapping("getMenuRoleList")
    public ResponseResult getMenuRoleList(){
        ResponseResult responseResult=new ResponseResult();
        List<Map<String,Object>> getMenuRoleList=roleMapper.getMenuRoleList();
        responseResult.setResult(getMenuRoleList);
        return responseResult;
    }


}
