package com.siti.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

/**
 * Created by ht on 2018/2/7.
 * 邮件发送工具类
 */
public final class MailUtil {
    private final static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private final static String ssl_port = "465";
    private final static String smtp = "smtp.163.com";
    private final static String userName = "lujiazuisiti@163.com";
    private final static String pwd = "lujiazui123";

    /***
     * 有些服务器，如阿里云，会禁用25端口，只能用465端口发送
     * */
    public static int sendMailBySSL(String subject, String content, String email) {
        try {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
            props.setProperty("mail.smtp.host", smtp);
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", ssl_port);
            props.setProperty("mail.smtp.socketFactory.port", ssl_port);
            props.setProperty("mail.smtp.auth", "true");
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, pwd);
                }
            });
            session.setDebug(true);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
            message.setSubject(subject);
            message.setText(content);
            message.setSentDate(new Date());
            Transport transport = session.getTransport();
            transport.connect(userName, pwd);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return 0;
        } catch (Exception e) {
            return -1;
        }

    }

}
