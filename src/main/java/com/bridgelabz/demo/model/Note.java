package com.bridgelabz.demo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fundoo_notes")
@Getter
@Setter
@NoArgsConstructor
public class Note {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "User Id cannot be empty or null")
	private Long userId;
	@NotEmpty(message = "Notes title cannot be empty or null")
	private String notesTitle;
	@NotEmpty(message = "Notes description cannot be empty or null")
	private String notesDescription;
	private int pin = 0;
	private Date remindMe;
	private String color = "white";
	private int archive = 0;
	private int trash = 0;

}
