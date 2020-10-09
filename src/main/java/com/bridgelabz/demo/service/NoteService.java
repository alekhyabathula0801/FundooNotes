package com.bridgelabz.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.model.Collabrator;
import com.bridgelabz.demo.model.Note;
import com.bridgelabz.demo.model.Response;
import com.bridgelabz.demo.repository.CollabratorRepository;
import com.bridgelabz.demo.repository.LabelNotesMappingRepository;
import com.bridgelabz.demo.repository.NoteRepository;
import com.bridgelabz.demo.repository.UserRepository;

@Service
public class NoteService {

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private LabelNotesMappingRepository labelNotesMappingRepository;

	@Autowired
	private CollabratorRepository collabratorRepository;

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
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.TRY_AGAIN_LATER, null, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> getAllNotesByUserId(Long userId, boolean isTrash) {
		try {
			userRepository.findById(userId).get();
			List<Note> availableNotes = noteRepository.findAllByUserId(userId).stream()
					.filter(note -> note.getTrash() == isTrash && !note.getArchive()).collect(Collectors.toList());
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, availableNotes, 200),
					HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.TRY_AGAIN_LATER, null, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> getAllArchiveNotesByUserId(Long userId) {
		try {
			userRepository.findById(userId).get();
			List<Note> availableNotes = noteRepository.findAllByUserId(userId).stream()
					.filter(note -> !note.getTrash() && note.getArchive()).collect(Collectors.toList());
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, availableNotes, 200),
					HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.TRY_AGAIN_LATER, null, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> setIsTrash(Long noteId, boolean isTrash) {
		try {
			Note note = noteRepository.findById(noteId).get();
			note.setTrash(isTrash);
			Note notes = noteRepository.save(note);
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, notes, 200), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.TRY_AGAIN_LATER, null, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> deleteNote(Long noteId) {
		try {
			if (labelNotesMappingRepository.findByNoteId(noteId) != null)
				labelNotesMappingRepository.deleteByNoteId(noteId);
			if (collabratorRepository.findByNoteId(noteId) != null)
				collabratorRepository.deleteByNoteId(noteId);
			noteRepository.deleteById(noteId);
			return new ResponseEntity<Response>(
					userService.getResponse(Message.SUCCESSFUL, Message.NOTE_DELETED_SUCCESFULLY, 200), HttpStatus.OK);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.TRY_AGAIN_LATER, null, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> updateNote(Note note) {
		try {
			userRepository.findById(note.getUserId()).get();
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		}
		try {
			noteRepository.findById(note.getId()).get();
			Note noteDetails = noteRepository.save(note);
			return new ResponseEntity<Response>(
					userService.getResponse(Message.NOTE_UPDATED_SUCCESFULLY, noteDetails, 200), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.TRY_AGAIN_LATER, null, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> addCollabrator(Collabrator collabrator) {
		try {
			noteRepository.findById(collabrator.getNoteId()).get();
			Collabrator collabrators = collabratorRepository.save(collabrator);
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, collabrators, 200),
					HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.TRY_AGAIN_LATER, null, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

}
