package com.bridgelabz.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.model.Login;
import com.bridgelabz.demo.model.User;
import com.bridgelabz.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

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

}
