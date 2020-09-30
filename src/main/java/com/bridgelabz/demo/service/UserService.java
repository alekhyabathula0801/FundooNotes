package com.bridgelabz.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.demo.enumeration.Message;
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

		}
		return Message.EMAIL_ID_ALREADY_EXISTS;
	}

}
