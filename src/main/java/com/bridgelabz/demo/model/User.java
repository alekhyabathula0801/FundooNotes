package com.bridgelabz.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Entity
@Table(name = "user_details")
@Getter
@Setter
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(message = "First name cannot be empty or null")
	@Pattern(regexp = "[A-Z][a-z]{2,}", message = "First name must start with uppercase follwed by lowercase with minimum of 3 characters")
	private String firstName;
	@NotEmpty(message = "Last name cannot be empty or null")
	@Pattern(regexp = "[A-Z][a-z]{2,}", message = "Last name must start with uppercase follwed by lowercase with minimum of 3 characters")
	private String lastName;
	@NotEmpty(message = "Email cannot be empty or null")
	@Pattern(regexp = "^[a-zA-Z0-9]+([._+-][0-9a-zA-Z]+)*@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}([.][a-zA-Z]{2,3})?$", message = "Email must be in the form of - char@char.com or char@char.com.in")
	private String email;
	@NotEmpty(message = "Pasword cannot be empty or null")
	@Pattern(regexp = "(?=.*[A-Z])(?=.*[^0-9a-zA-Z])(?=.*[0-9]).{8,}", message = "Password must contain atleast one capital letter, special character and number with minimum of 8 characters")
	private String password;
	@NotEmpty(message = "Mobile number cannot be empty or null")
	@Pattern(regexp = "[0-9]{5,10}", message = "Mobile number must contain 5-10 digits")
	private String mobile;
	private int isVerified;

	public User(String firstName, String lastName, String email, String password, String mobile, int is_verified) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.mobile = mobile;
		this.isVerified = is_verified;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", mobile=" + mobile + ", is_verified=" + isVerified + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isVerified != other.isVerified)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (mobile != other.mobile)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

}
