package com.bridgelabz.demo.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Login {
	
	@NotEmpty(message = "Email cannot be empty or null")
	@Pattern(regexp = "^[a-zA-Z0-9]+([._+-][0-9a-zA-Z]+)*@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}([.][a-zA-Z]{2,3})?$", message = "Email must be in the form of - char@char.com or char@char.com.in")
	private String email;
	@NotEmpty(message = "Password cannot be empty or null")
	@Pattern(regexp = "(?=.*[A-Z])(?=.*[^0-9a-zA-Z])(?=.*[0-9]).{8,}", message = "Password must contain atleast one capital letter, special character and number with minimum of 8 characters")
	private String password;

}
