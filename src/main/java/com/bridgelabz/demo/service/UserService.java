package com.bridgelabz.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.model.Login;
import com.bridgelabz.demo.model.User;
import com.bridgelabz.demo.repository.UserRepository;
import com.bridgelabz.demo.util.Utility;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	public UserService() {

	}

	public Message addUser(User user) {
		try {
			User userDetails = userRepository.findByEmail(user.getEmail());
			if (userDetails == null) {
				userRepository.save(user);
				return Message.USER_ADDED;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Message.EMAIL_ID_ALREADY_EXISTS;
	}

	public User userLogin(Login login) {
		User user = userRepository.findByEmail(login.getEmail());
		if (user != null) {
			if (user.getPassword().equals(login.getPassword()) & user.getIsVerified() == 1)
				return user;
		}
		return null;
	}

	public Message validateUser(Login login) {
		User user = userRepository.findByEmail(login.getEmail());
		if (user != null) {
			if (user.getPassword().equals(login.getPassword())) {
				if (user.getIsVerified() != 1)
					return Message.EMAIL_HAS_NOT_VERIFIED;
			}
			return Message.ENTERED_WRONG_PASSWORD;
		}
		return Message.EMAIL_ID_DOESNT_EXISTS;
	}

	public Message sendEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			if (emailService.sendMail(email, "Click on link to reset password \n" + Utility.getBody("reset_password",user.getId()),
					"Recovery password for Fundoo App"))
				return Message.EMAIL_SENT_SUCCESSFULLY;
			return Message.SERVER_SIDE_PROBLEM;
		}
		return Message.EMAIL_ID_DOESNT_EXISTS;
	}

}
