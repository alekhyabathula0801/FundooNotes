package com.bridgelabz.demo.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long noteId;

	@NotEmpty(message = "Notes title cannot be empty or null")
	@Column(nullable = false, length = 100)
	private String title;

	@NotEmpty(message = "Notes description cannot be empty or null")
	@Column(nullable = false, length = 800)
	private String description;

	@Column(nullable = false, length = 15)
	private String color = "white";
	private boolean isPinned = false;
	private boolean isArchive = false;
	private boolean isTrash = false;
	private Date remainder = null;

	@JsonIgnore
	@NotNull
	private LocalDateTime createdDate = LocalDateTime.now();

	@JsonIgnore
	@NotNull
	private LocalDateTime modifiedDate = LocalDateTime.now();

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToMany
	@JoinTable(name = "note_label", joinColumns = { @JoinColumn(name = "note_id") }, inverseJoinColumns = {
			@JoinColumn(name = "label_id") })
	private Set<Label> label;

	@ManyToMany
	@JoinTable(name = "collaborate_user", joinColumns = { @JoinColumn(name = "note_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
	private Set<User> collaboratedUser;

}
