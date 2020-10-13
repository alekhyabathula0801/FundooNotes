package com.bridgelabz.demo.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_details")
@Getter
@Setter
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	
	@NotEmpty(message="Please fill the first name")
	@Column(nullable=false, length = 25)
	private String firstName;
	
	@NotEmpty(message="Please fill the last name")
	@Column(nullable=false, length = 25)
	private String lastName;
	
	@Column(unique = true, nullable=false, length = 45)
	@NotEmpty(message="Please fill the email")
	@Email(message="Please enter valid email id")
	private String email;
	
	@Column(nullable=false)
	@NotEmpty(message="Please fill the mobile number")
	@Pattern(regexp = "[0-9]{10}", message = "Number should be 10 digit only")
	private String mobileNumber;
	
	@NotEmpty(message="Please fill the password")
	@Column(nullable=false, length = 25)
	@Pattern(regexp = "(?=.*[A-Z])(?=.*[^0-9a-zA-Z])(?=.*[0-9]).{8,}", message = "Password must contain atleast one capital letter, special character and number with minimum of 8 characters")
	private String password;
	
	@JsonIgnore
	@Column(nullable=false)
	@CreationTimestamp
	private LocalDateTime createdDate = LocalDateTime.now();
	
	@JsonIgnore
	@Column(nullable=false)
	@UpdateTimestamp
	private LocalDateTime modifiedDate = LocalDateTime.now();
	
	private boolean isVerified = false;
	
	@ManyToMany(mappedBy="collaboratedUser")
	@JsonIgnore
	private Set<Note> collaboratedNote;

}
