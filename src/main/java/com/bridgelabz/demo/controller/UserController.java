package com.bridgelabz.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.model.Response;
import com.bridgelabz.demo.model.User;
import com.bridgelabz.demo.service.UserService;

@RestController
@RequestMapping(path = "fundoo")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	Response response;
	
	@PostMapping(path = "/add_user")
	public ResponseEntity<Response> addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<String>();
			for(ObjectError error : bindingResult.getAllErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			response.setMessage(Message.BAD_REQUEST);
			response.setResult(errorMessages);
			response.setStatus(404);
			return  new ResponseEntity<Response>(response,HttpStatus.BAD_REQUEST);
		}
		User addedUser = userService.addUser(user);
		response.setMessage(Message.SUCCESSFUL);
		response.setResult(addedUser);
		response.setStatus(200);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}

}
