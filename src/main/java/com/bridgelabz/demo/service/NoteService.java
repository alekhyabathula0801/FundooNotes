package com.bridgelabz.demo.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.model.Note;
import com.bridgelabz.demo.model.Response;
import com.bridgelabz.demo.repository.NoteRepository;
import com.bridgelabz.demo.repository.UserRepository;

@Service
public class NoteService {

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	public NoteService() {

	}

	public ResponseEntity<Response> addNote(Note note) {
		try {
			userRepository.findById(note.getUserId()).get();
			Note noteDetails = noteRepository.save(note);
			return new ResponseEntity<Response>(
					userService.getResponse(Message.NOTE_ADDED_SUCCESFULLY, noteDetails, 200),
					HttpStatus.SERVICE_UNAVAILABLE);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(
					userService.getResponse(Message.NOT_FOUND, Message.USER_ID_DOESNT_EXISTS, 404),
					HttpStatus.SERVICE_UNAVAILABLE);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(
				userService.getResponse(Message.SERVER_SIDE_PROBLEM, Message.TRY_AGAIN_LATER, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

}
