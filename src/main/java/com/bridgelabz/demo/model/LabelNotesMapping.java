package com.bridgelabz.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "label_notes_mapping")
@Getter
@Setter
@NoArgsConstructor
public class LabelNotesMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Label Id cannot be empty or null")
	private Long labelId;
	@NotNull(message = "Note Id cannot be empty or null")
	private Long noteId;
	
}
