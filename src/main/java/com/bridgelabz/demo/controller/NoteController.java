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
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.model.Response;
import com.bridgelabz.demo.model.Note;
import com.bridgelabz.demo.service.NoteService;
import com.bridgelabz.demo.service.UserService;

@RestController
@RequestMapping(path = "fundoo")
public class NoteController {

	@Autowired
	UserService userService;

	@Autowired
	NoteService noteService;

	@PostMapping(path = "/add_note")
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

}
