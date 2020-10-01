package com.bridgelabz.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class EmailConfiguration {
	@Bean
	public SimpleMailMessage emailTemplate() {
		return new SimpleMailMessage();
	}
}
