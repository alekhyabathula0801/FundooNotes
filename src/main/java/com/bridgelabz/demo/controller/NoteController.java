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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bridgelabz.demo.dto.Collabrator;
import com.bridgelabz.demo.dto.Response;
import com.bridgelabz.demo.enumeration.Message;
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
	public ResponseEntity<Response> addNote(@Valid @RequestBody Note note, @RequestHeader("token") String token,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			return new ResponseEntity<Response>(userService.getResponse(Message.CONFLICT, errorMessages, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.addNote(note, token);
	}

	@PutMapping(path = "/update_note")
	@ApiOperation(value = "Update Note")
	public ResponseEntity<Response> updateNote(@Valid @RequestBody Note note, @RequestHeader("token") String token,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			return new ResponseEntity<Response>(userService.getResponse(Message.CONFLICT, errorMessages, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.updateNote(note, token);
	}

	@PutMapping(path = "/update_color")
	@ApiOperation(value = "Update note color")
	public ResponseEntity<Response> updateColor(@RequestHeader("token") String token, @RequestParam String color,
			@RequestParam Long noteId) {
		if (token == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		if (noteId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		if (color == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.COLOR_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.updateColor(noteId, color, token);
	}

	@PutMapping(path = "/archive_note")
	@ApiOperation(value = "Add or remove from archive notes")
	public ResponseEntity<Response> archiveNote(@RequestHeader("token") String token, @RequestParam Long noteId) {
		if (token == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		if (noteId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.archiveNote(noteId, token);
	}
	
	@PutMapping(path = "/pin_note")
	@ApiOperation(value = "Add or remove from pinned notes")
	public ResponseEntity<Response> pinNote(@RequestHeader("token") String token, @RequestParam Long noteId) {
		if (token == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		if (noteId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.pinNote(noteId, token);
	}

	@PutMapping(path = "/add_to_trash")
	@ApiOperation(value = "Add to trash")
	public ResponseEntity<Response> addToTrash(@RequestParam Long noteId, @RequestHeader("token") String token) {
		if (noteId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.setIsTrash(noteId, true, token);
	}

	@PutMapping(path = "/restore_note")
	@ApiOperation(value = "Restore note")
	public ResponseEntity<Response> restoreNote(@RequestParam Long noteId, @RequestHeader("token") String token) {
		if (noteId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.setIsTrash(noteId, false, token);
	}

	@DeleteMapping(path = "/delete_note")
	@ApiOperation(value = "Delete note")
	public ResponseEntity<Response> deleteNote(@RequestParam Long noteId, @RequestHeader("token") String token) {
		if (noteId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.deleteNote(noteId, token);
	}

	@GetMapping(path = "/get_all_notes")
	@ApiOperation(value = "Get all notes by user id")
	public ResponseEntity<Response> getAllNotes(@RequestHeader String token) {
		if (token == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.getAllNotesByUserId(token, false);
	}

	@GetMapping(path = "/get_all_notes_in_bin")
	@ApiOperation(value = "Get all notes in bin by user id ")
	public ResponseEntity<Response> getAllNotesInBin(@RequestHeader("token") String token) {
		if (token == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.getAllNotesByUserId(token, true);
	}

	@GetMapping(path = "/get_all_archive_notes")
	@ApiOperation(value = "Get all archive notes by user id")
	public ResponseEntity<Response> getAllArchiveNotes(@RequestHeader("token") String token) {
		if (token == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.getAllArchiveNotesByUserId(token);
	}

	@PostMapping(path = "/add_collabrator")
	@ApiOperation(value = "Add Collabrator")
	public ResponseEntity<Response> addCollabrator(@Valid @RequestBody Collabrator collabrator,
			@RequestHeader("token") String token, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			return new ResponseEntity<Response>(userService.getResponse(Message.CONFLICT, errorMessages, 409),
					HttpStatus.CONFLICT);
		}
		return noteService.addCollabrator(collabrator, token);
	}

}
