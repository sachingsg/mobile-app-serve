package com.gsg.mail;

import java.io.IOException;

import javax.mail.MessagingException;

public interface MailSender {

	void sendMessage(String to, String from, String sub, String msg);

	void sendMessageWithAttachment(String to, String from, String sub, String msg, String... attachments) throws MessagingException;



	void sendMessageWithAttachment(String to, String from, String sub, String msg, byte[] dataByte,
			String attachFileName) throws MessagingException, IOException;

}
