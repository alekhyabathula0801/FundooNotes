package com.bridgelabz.demo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@NoArgsConstructor
public class Collabrator {
	
	@NotEmpty(message = "Email cannot be empty or null")
	@Pattern(regexp = "^[a-zA-Z0-9]+([._+-][0-9a-zA-Z]+)*@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}([.][a-zA-Z]{2,3})?$", message = "Email must be in the form of - char@char.com or char@char.com.in")
	private String collabratorEmail;
	@NotNull(message = "Note Id cannot be null")
	private Long noteId;

}
