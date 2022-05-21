package com.troublemaker.utils.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Troublemaker
 * @date 2022- 04 24 13:23
 */
@Component
public class SendMail {
    private JavaMailSender javaMailSender;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMail(String email, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("疫情打卡小助手");
        message.setFrom("1807366859@qq.com");
        message.setTo(email);
        message.setSentDate(new Date());
        message.setText(text);
        javaMailSender.send(message);
    }
}
