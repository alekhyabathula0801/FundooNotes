package com.bridgelabz.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.model.Response;
import com.bridgelabz.demo.model.Label;
import com.bridgelabz.demo.model.Note;
import com.bridgelabz.demo.service.NoteService;
import com.bridgelabz.demo.service.UserService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(path = "fundoo")
public class NoteController {

	@Autowired
	UserService userService;

	@Autowired
	NoteService noteService;

	@PostMapping(path = "/add_note")
	@ApiOperation(value = "Save Note")
	public ResponseEntity<Response> addUser(@Valid @RequestBody Note note, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			return new ResponseEntity<Response>(userService.getResponse(Message.CONFLICT, errorMessages, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.addNote(note);
	}

	@GetMapping(path = "/get_all_notes")
	@ApiOperation(value = "Get all notes by user id")
	public ResponseEntity<Response> getAllNotes(@RequestParam Long userId) {
		if (userId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.getAllNotesByUserId(userId);
	}
	
	@PostMapping(path = "/add_label")
	@ApiOperation(value = "Add label")
	public ResponseEntity<Response> addLabel(@Valid @RequestBody Label label, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			return new ResponseEntity<Response>(userService.getResponse(Message.CONFLICT, errorMessages, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.addLabel(label);
	}

	@GetMapping(path = "/get_all_labels")
	@ApiOperation(value = "Get all labels by user id")
	public ResponseEntity<Response> getAllLabels(@RequestParam Long userId) {
		if (userId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.getAllLabelsByUserId(userId);
	}
	
}
