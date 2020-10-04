package com.bridgelabz.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.model.Login;
import com.bridgelabz.demo.model.Response;
import com.bridgelabz.demo.model.User;
import com.bridgelabz.demo.repository.UserRepository;
import com.bridgelabz.demo.util.UserToken;
import com.bridgelabz.demo.util.Utility;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserToken userToken;

	@Autowired
	private Utility utility;

	@Autowired
	Response response;

	public UserService() {

	}

	public Message addUser(User user) {
		try {
			User userDetails = userRepository.findByEmail(user.getEmail());
			if (userDetails == null) {
				userRepository.save(user);
				sendEmailVerification(user.getEmail());
				return Message.USER_ADDED_AND_VERIFY_EMAIL_TO_CONTINUE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Message.EMAIL_ID_ALREADY_EXISTS;
	}

	public User userLogin(Login login) {
		User user = userRepository.findByEmail(login.getEmail());
		if (user != null) {
			if (user.getPassword().equals(login.getPassword()) & user.getIsVerified())
				return user;
		}
		return null;
	}

	public Message validateUser(Login login) {
		User user = userRepository.findByEmail(login.getEmail());
		if (user != null) {
			if (user.getPassword().equals(login.getPassword())) {
				if (!user.getIsVerified())
					return Message.EMAIL_HAS_NOT_VERIFIED;
			}
			return Message.ENTERED_WRONG_PASSWORD;
		}
		return Message.EMAIL_ID_DOESNT_EXISTS;
	}

	public Message sendEmailToRecoverPassword(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			if (emailService.sendMail(email,
					"Click on link to reset password \n" + utility.getBody("reset_password", user.getId()),
					"Recovery password for Fundoo App"))
				return Message.EMAIL_SENT_SUCCESSFULLY;
			return Message.SERVER_SIDE_PROBLEM;
		}
		return Message.EMAIL_ID_DOESNT_EXISTS;
	}

	public Message resetPassword(String password, String token) {
		Long userId = userToken.getUserIdFromToken(token);
		if (userId != null) {
			User user = userRepository.findById(userId).get();
			if (user != null) {
				user.setPassword(password);
				userRepository.save(user);
				return Message.PASSWORD_UPDATED_SUCCESSFULLY_PLEASE_LOGIN_TO_CONTINUE;
			}
		}
		return Message.INVALID_USER;
	}

	public Response getResponse(Message message, Object result, int status) {
		response.setMessage(message);
		response.setResult(result);
		response.setStatus(status);
		return response;
	}

	public Message sendEmailVerification(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			if (emailService.sendMail(email,
					"Click on link to verify email \n" + utility.getBody("verify_email", user.getId()),
					"Email verification for Fundoo App"))
				return Message.EMAIL_SENT_SUCCESSFULLY;
			return Message.SERVER_SIDE_PROBLEM;
		}
		return Message.EMAIL_ID_DOESNT_EXISTS;
	}

	public Message verifyEmail(String token) {
		Long userId = userToken.getUserIdFromToken(token);
		if (userId != null) {
			User user = userRepository.findById(userId).get();
			if (user != null) {
				user.setIsVerified(true);
				userRepository.save(user);
				return Message.EMAIL_VERIFIED_SUCCESSFULLY_PLEASE_LOGIN_TO_CONTINUE;
			}
		}
		return Message.INVALID_USER;
	}

}
