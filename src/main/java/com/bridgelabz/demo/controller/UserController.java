package com.bridgelabz.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.demo.model.User;
import com.bridgelabz.demo.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping(path = "fundoo/add_user")
	public User addUser(@RequestBody User user) {
		return userService.addUser(user);
	}

}
