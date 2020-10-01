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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.model.Login;
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
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			return new ResponseEntity<Response>(getResponse(Message.CONFLICT, errorMessages, 409), HttpStatus.CONFLICT);
		}
		Message result = userService.addUser(user);
		if (result.equals(Message.USER_ADDED))
			return new ResponseEntity<Response>(getResponse(Message.SUCCESSFUL, result, 200), HttpStatus.OK);
		return new ResponseEntity<Response>(getResponse(Message.CONFLICT, result, 409), HttpStatus.CONFLICT);
	}

	@PostMapping(path = "/login")
	public ResponseEntity<Response> loginUser(@Valid @RequestBody Login login, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			return new ResponseEntity<Response>(getResponse(Message.CONFLICT, errorMessages, 409), HttpStatus.CONFLICT);
		}
		User user = userService.userLogin(login);
		if (user != null)
			return new ResponseEntity<Response>(getResponse(Message.USER_LOGGED_IN_SUCCESFULL, user.getId(), 200),
					HttpStatus.OK);
		Message message = userService.validateUser(login);
		return new ResponseEntity<Response>(getResponse(Message.BAD_REQUEST, message, 404), HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "/forgot_password")
	public ResponseEntity<Response> forgotPassword(@RequestParam String email) {
		if (!email.matches("^[a-zA-Z0-9]+([._+-][0-9a-zA-Z]+)*@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}([.][a-zA-Z]{2,3})?$")) {
			String errorMessage = "Email must be in the form of - char@char.com or char@char.com.in";
			return new ResponseEntity<Response>(getResponse(Message.CONFLICT, errorMessage, 409), HttpStatus.CONFLICT);
		}
		Message message = userService.sendEmail(email);
		if (message.equals(Message.EMAIL_SENT_SUCCESSFULLY))
			return new ResponseEntity<Response>(getResponse(Message.SUCCESSFUL, message, 200), HttpStatus.OK);
		if (message.equals(Message.EMAIL_ID_DOESNT_EXISTS))
			return new ResponseEntity<Response>(getResponse(Message.NOT_FOUND, message, 404), HttpStatus.NOT_FOUND);
		return new ResponseEntity<Response>(getResponse(Message.TRY_AGAIN_LATER, message, 500),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public Response getResponse(Message message, Object result, int status) {
		response.setMessage(message);
		response.setResult(result);
		response.setStatus(status);
		return response;
	}

}
