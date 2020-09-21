package com.zbf.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.util.IOUtils;

import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @author: LH
 * @date: 2020/6/20 17:18
 * @version: V1.0
 **/
public class PoiUtil {



        /**
         * 导出到excel中
         * @param connection  与数据库的连接
         * @param sql  查询的sql语句 select * from 表名
         * @param tableName excel的sheet名
         * @param filePath  输出的文件路径
         */
        public static void exportToExcel(Connection connection, String sql, String tableName, String filePath){

            ResultSet resultSet = null;
            PreparedStatement statement = null;
            OutputStream os=null;

            //创建一个excel工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();

            //创建表 并设置sheet的名称
            HSSFSheet sheet = workbook.createSheet(tableName);

            //创建标题行
            HSSFRow row = sheet.createRow(0);

            //日期格式化
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            HSSFCreationHelper creationHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));

            try {

                //通过传进来的sql 查询表中的数据
                statement = connection.prepareStatement(sql);
                //获取结果集
                resultSet = statement.executeQuery();
                //获取元数据    用来获取字段名
                ResultSetMetaData metaData = resultSet.getMetaData();
                //每一行的列数
                int columnCount = metaData.getColumnCount();

                //动态根据字段名设置列名 即标题
                for (int i = 0; i < columnCount; i++) {
                    String labelName = metaData.getColumnLabel(i + 1);
                    row.createCell(i).setCellValue(labelName);
                }

                int i=1;

                //临时行
                HSSFRow tempRow;
                //遍历结果集  往excel中写数据
                while (resultSet.next()){
                    //创建临时行  即当前行
                    tempRow = sheet.createRow(i);
                    for (int j = 0; j < columnCount; j++) {
                        //获取当前单元格
                        HSSFCell tempCell = tempRow.createCell(j);
                        //获取数据库中与当前格对应的数据
                        Object temp=resultSet.getObject(j+1);

                        //如果获取到的数据为空则跳过该单元格
                        if (temp==null || "".equals(temp)){
                            continue;
                        }

                        //如果获取到的是时间  则格式化时间再写到excel中
                        if (temp instanceof java.util.Date){
                            //给日期设置样式
                            tempCell.setCellStyle(cellStyle);
                            tempCell.setCellValue((java.util.Date) temp);
                        }else if(temp instanceof Boolean){
                            tempCell.setCellValue((Boolean) temp);
                        }else if (temp instanceof Double){
                            tempCell.setCellValue((Double) temp);
                        }else {
                            tempCell.setCellValue(temp.toString());
                        }
                    }
                    i++;
                }

                os=new BufferedOutputStream(new FileOutputStream(filePath));
                //将excel表格写出到指定的路径下
                workbook.write(os);
                System.out.println(filePath+"导出成功");

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                //关闭资源
                IOUtils.closeQuietly(os);
                if (resultSet!=null){
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (connection!=null){
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }




    /**
     *
     * 从excel导入到mysql中
     * @param t 与数据库中要插入的表对应的实体类
     * @param file   excel文件流
     * @param <T>
     * @return  实体类的list集合
     */
    public static <T> List<T> importToMysql(Class<T> t, InputStream file){

        //准备一个list用来存放结果
        List<T> list=new ArrayList<>();

        HSSFWorkbook workbook= null;
        try {
            //通过文件路径创建一个工工作簿
            workbook = new HSSFWorkbook(file);
        } catch (IOException e) {
            System.out.println("文件读取失败");
            System.out.println(e.getMessage());
        }

        //获取当前excel的第一个sheet表格  如果有多个sheet就自行遍历
        HSSFSheet sheet = workbook.getSheetAt(0);

        //获取excel表的第一行   用来获取表的字段名
        HSSFRow tempRow = sheet.getRow(0);

        //获取总的行数
        int lastRowNum = sheet.getLastRowNum();

        //遍历行    从第二行开始   第一行为表的字段名
        for (int i = 1; i <= lastRowNum; i++) {
            HSSFRow row = sheet.getRow(i);

            //获取列数
            int cells = row.getPhysicalNumberOfCells();

            //实例化实体类对象
            T tempT= null;
            try {
                tempT = t.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            //遍历当前行的每一列
            for (int j = 0; j < cells; j++) {
                //当前列的值
                HSSFCell hsscell = row.getCell(j);
                //当前列的名称
                HSSFCell cellName= tempRow.getCell(j);

                Field field = null;
                try {
                    //根据列名获取实体类与之对应的属性
                    field = tempT.getClass().getDeclaredField(cellName.toString());
                } catch (NoSuchFieldException e) {
                    System.out.println(field.getName()+"属性获取失败");
                    System.out.println(e.getMessage());
                }

                //如果该字段为空  则跳过
                if (hsscell==null){
                    continue;
                }

                try {
                    //根据从excel获取到的数据的类型设值
                    if (field!=null){
                        //私有属性授权
                        field.setAccessible(true);
                        if (hsscell.getCellTypeEnum()== CellType.NUMERIC){
                            if(HSSFDateUtil.isCellDateFormatted(hsscell)){//日期
                                if (field.getType()== Date.class){
                                    field.set(tempT,hsscell.getDateCellValue());
                                }
                            }else if(field.getType()==Integer.class){
                                //int类型
                                field.set(tempT,Integer.valueOf(hsscell.getStringCellValue()));
                            }else if(field.getType()==Double.class){
                                //double类型
                                field.set(tempT,Double.parseDouble(hsscell.toString()) );
                            }
                        }else if (hsscell.getCellTypeEnum()==CellType.BOOLEAN){
                            //布尔值
                            if (field.getType()==Boolean.class){
                                field.set(tempT,hsscell.getBooleanCellValue() );
                            }
                        }else if(hsscell.getCellTypeEnum()==CellType.STRING){
                            if (field.getType()==Integer.class){
                                field.set(tempT,Integer.parseInt(hsscell.toString()));
                            }else if (field.getType()==Double.class){
                                field.set(tempT,Double.valueOf(hsscell.toString()) );
                            }else if (field.getType()==String.class){
                                field.set(tempT,hsscell.toString() );
                            }else if(HSSFDateUtil.isCellDateFormatted(hsscell)){
                                if (field.getType()== Date.class){
                                    field.set(tempT,hsscell.getDateCellValue());
                                }
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    System.out.println(field.getName()+"设值失败");
                    e.printStackTrace();
                }
            }
            //添加到list集合中
            list.add(tempT);
        }
        //将封装好的list集合返回
        return list;
    }





}
