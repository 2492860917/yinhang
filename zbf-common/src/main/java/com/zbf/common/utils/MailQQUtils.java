package com.zbf.common.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 作者: LCG
 * 日期: 2020/6/15 16:27
 * 描述:
 */
public class MailQQUtils {
    /**
     * 邮件协议
     */
    private static String protocol="smtp";
    /**
     * 邮件服务的主机
     */
    private static String host="smtp.qq.com";
    /**
     * 邮件服务的端口
     */
    private static String port="25";
    /**
     * 是否打印出debug信息
     */
    private static String debug="false";

    /**
     * 发件人
     */
    private static String from="927613833@qq.com";
    /**
     * 发送邮件的授权码
     */
    private static String authCode="foxmwwzkdylubbed";


    /**
     * 发送邮件
     * @param to  接收的邮箱
     * @param code 激活码
     * @param webName  公司的名字
     * @param linkhref 激活的链接
     */
 public static void sendMessage(String to,String code,String webName,String linkhref){
     try {
         Properties prop = new Properties();
         prop.setProperty("mail.transport.protocol", protocol);
         prop.setProperty("mail.smtp.host", host);
         prop.setProperty("mail.smtp.auth", "true");
         prop.put("mail.smtp.port",port);
         prop.setProperty("mail.debug", debug);
         Authenticator authenticator = new Authenticator() {
             @Override
             protected PasswordAuthentication getPasswordAuthentication() {

                 return new PasswordAuthentication(from,authCode);
             }
         };
         //创建会话
         Session session = Session.getInstance(prop,authenticator);
         //填写信封写信
         Message msg = new MimeMessage(session);
         msg.setFrom(new InternetAddress(from));
         msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
         msg.setSubject(webName+"激活邮箱!");
         msg.setText(linkhref);
         //验证用户名密码发送邮件
         Transport.send(msg);
     }catch (Exception e){
         e.printStackTrace();
     }
 }


    public static void main(String[] args) {
        sendMessage("2492860917@qq.com","65456465656","三豆网络","");
    }

}
