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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bridgelabz.demo.dto.Response;
import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.model.Collabrator;
import com.bridgelabz.demo.model.Note;
import com.bridgelabz.demo.service.NoteService;
import com.bridgelabz.demo.service.UserService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(path = "note")
public class NoteController {

	@Autowired
	UserService userService;

	@Autowired
	NoteService noteService;

	@PostMapping(path = "/add_note")
	@ApiOperation(value = "Save Note")
	public ResponseEntity<Response> addNote(@Valid @RequestBody Note note, BindingResult bindingResult) {
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

	@PutMapping(path = "/update_note")
	@ApiOperation(value = "Update Note")
	public ResponseEntity<Response> updateNote(@Valid @RequestBody Note note, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			return new ResponseEntity<Response>(userService.getResponse(Message.CONFLICT, errorMessages, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.updateNote(note);
	}

	@PutMapping(path = "/add_to_trash")
	@ApiOperation(value = "Add to trash")
	public ResponseEntity<Response> addToTrash(@RequestParam Long noteId) {
		if (noteId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.setIsTrash(noteId, true);
	}

	@PutMapping(path = "/restore_note")
	@ApiOperation(value = "Restore note")
	public ResponseEntity<Response> restoreNote(@RequestParam Long noteId) {
		if (noteId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.setIsTrash(noteId, false);
	}

	@DeleteMapping(path = "/delete_note")
	@ApiOperation(value = "Delete note")
	public ResponseEntity<Response> deleteNote(@RequestParam Long noteId) {
		if (noteId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.deleteNote(noteId);
	}

	@GetMapping(path = "/get_all_notes")
	@ApiOperation(value = "Get all notes by user id")
	public ResponseEntity<Response> getAllNotes(@RequestParam Long userId) {
		if (userId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.getAllNotesByUserId(userId, false);
	}

	@GetMapping(path = "/get_all_notes_in_bin")
	@ApiOperation(value = "Get all notes in bin by user id ")
	public ResponseEntity<Response> getAllNotesInBin(@RequestParam Long userId) {
		if (userId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.getAllNotesByUserId(userId, true);
	}

	@GetMapping(path = "/get_all_archive_notes")
	@ApiOperation(value = "Get all archive notes by user id")
	public ResponseEntity<Response> getAllArchiveNotes(@RequestParam Long userId) {
		if (userId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.getAllArchiveNotesByUserId(userId);
	}

	@PostMapping(path = "/add_collabrator")
	@ApiOperation(value = "Add Collabrator")
	public ResponseEntity<Response> addCollabrator(@Valid @RequestBody Collabrator collabrator,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			return new ResponseEntity<Response>(userService.getResponse(Message.CONFLICT, errorMessages, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.addCollabrator(collabrator);
	}

}
