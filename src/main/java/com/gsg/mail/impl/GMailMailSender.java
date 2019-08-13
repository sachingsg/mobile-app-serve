package com.gsg.mail.impl;

import java.io.File;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.gsg.mail.MailSender;

@Component
public class GMailMailSender implements MailSender {

	@Autowired
	public JavaMailSender emailSender;

	@Override
	public void sendMessage(String to, String from, String sub, String msg) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(sub);
		message.setText(msg);
		emailSender.send(message);

	}

	@Override
	public void sendMessageWithAttachment(String to, String from, String sub, String msg, String... attachments)
			throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(to);
		helper.setSubject(sub);
		helper.setText(msg);

		FileSystemResource file = new FileSystemResource(new File(""));
		helper.addAttachment("Invoice", file);

		emailSender.send(message);

	}

	
	@Override
	public void sendMessageWithAttachment(String to, String from, String sub, String msg, byte[] dataByte, String attachFileName)
			throws MessagingException, IOException {
		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(to);
		helper.setCc("rupesh.panda@go-speedy-go.com");
		helper.setSubject(sub);
		helper.setText(msg);
		helper.addAttachment(attachFileName, new ByteArrayResource(dataByte));
		emailSender.send(message);

	}

}
