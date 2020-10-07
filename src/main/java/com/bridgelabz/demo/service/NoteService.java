package com.bridgelabz.demo.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.model.Label;
import com.bridgelabz.demo.model.Note;
import com.bridgelabz.demo.model.Response;
import com.bridgelabz.demo.repository.LabelRepository;
import com.bridgelabz.demo.repository.NoteRepository;
import com.bridgelabz.demo.repository.UserRepository;

@Service
public class NoteService {

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private LabelRepository labelRepository;

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
					userService.getResponse(Message.NOTE_ADDED_SUCCESFULLY, noteDetails, 200), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(
					userService.getResponse(Message.NOT_FOUND, Message.USER_ID_DOESNT_EXISTS, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(
				userService.getResponse(Message.SERVER_SIDE_PROBLEM, Message.TRY_AGAIN_LATER, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> getAllNotesByUserId(Long userId) {
		try {
			userRepository.findById(userId).get();
			List<Note> allNotes = noteRepository.findAllByUserId(userId);
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, allNotes, 200),
					HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(
					userService.getResponse(Message.NOT_FOUND, Message.USER_ID_DOESNT_EXISTS, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(
				userService.getResponse(Message.SERVER_SIDE_PROBLEM, Message.TRY_AGAIN_LATER, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> addLabel(Label label) {
		try {
			userRepository.findById(label.getUserId()).get();
			List<Label> allLabels = labelRepository.findAllByUserId(label.getUserId());
			if (allLabels.contains(label))
				return new ResponseEntity<Response>(userService.getResponse(Message.LABEL_NAME_EXISTS, null, 409),
						HttpStatus.CONFLICT);
			Label labels = labelRepository.save(label);
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, labels, 200),
					HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(
					userService.getResponse(Message.NOT_FOUND, Message.USER_ID_DOESNT_EXISTS, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(
				userService.getResponse(Message.SERVER_SIDE_PROBLEM, Message.TRY_AGAIN_LATER, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> getAllLabelsByUserId(Long userId) {
		try {
			userRepository.findById(userId).get();
			List<Label> allLabels = labelRepository.findAllByUserId(userId);
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, allLabels, 200),
					HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(
					userService.getResponse(Message.NOT_FOUND, Message.USER_ID_DOESNT_EXISTS, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(
				userService.getResponse(Message.SERVER_SIDE_PROBLEM, Message.TRY_AGAIN_LATER, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

}
