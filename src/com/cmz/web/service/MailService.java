package com.cmz.web.service;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.cmz.web.constant.GlobalConstant;


/**
 * 邮件发送服务
 * @author chenmingzhou
 *
 */
@Service
public class MailService {
	
	private JavaMailSenderImpl mailSender = null;
	
	@PostConstruct
	public void init(){
		mailSender = new JavaMailSenderImpl();
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		mailSender.setJavaMailProperties(props);
		
		mailSender.setHost(GlobalConstant.MAIL_HOST);
		mailSender.setUsername(GlobalConstant.MAIL_USER);
		mailSender.setPassword(GlobalConstant.MAIL_PWD);
		mailSender.setPort(GlobalConstant.MAIL_PORT);
	}
	
	public void sendMail(String receivers,String subject,String content){
		SimpleMailMessage msg = new SimpleMailMessage();
		String[] to = receivers.split(",");
		msg.setFrom(GlobalConstant.MAIL_USER);
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(content);
		mailSender.send(msg);
	}
}
