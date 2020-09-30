package com.bridgelabz.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.demo.model.User;
import com.bridgelabz.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public UserService() {
		
	}
	
	public User addUser(User user) {
		return userRepository.save(user);
	}
	
}
