package com.bridgelabz.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	SimpleMailMessage simpleMailMessage;

	public boolean sendMail(String email, String message, String subject) {
		try {
			simpleMailMessage.setTo(email);
			simpleMailMessage.setSubject(subject);
			simpleMailMessage.setText(message);
			javaMailSender.send(simpleMailMessage);
			return true;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return false;
	}
}
