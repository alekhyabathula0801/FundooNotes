package com.bridgelabz.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bridgelabz.demo.dto.Collabrator;
import com.bridgelabz.demo.dto.Response;
import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.exception.FundooNotesException;
import com.bridgelabz.demo.model.Note;
import com.bridgelabz.demo.model.User;
import com.bridgelabz.demo.repository.NoteRepository;
import com.bridgelabz.demo.repository.UserRepository;
import com.bridgelabz.demo.util.UserToken;

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

	public ResponseEntity<Response> addNote(Note note, String token) {
		Long userId = UserToken.getUserIdFromToken(token);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new FundooNotesException(Message.USER_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST));
		note.setUser(user);
		Note noteDetails = noteRepository.save(note);
		return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ADDED_SUCCESFULLY, noteDetails, 200),
				HttpStatus.OK);
	}

	public ResponseEntity<Response> getAllNotesByUserId(String token, boolean isTrash) {
		List<Note> availableNotes = new ArrayList<Note>();
		Long userId = UserToken.getUserIdFromToken(token);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new FundooNotesException(Message.USER_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST));
		List<Note> collaboratedNote = user.getCollaboratedNote().stream().collect(Collectors.toList());
		availableNotes = noteRepository.findAllNotesByUserId(userId, Boolean.valueOf(false), Boolean.valueOf(isTrash));
		System.out.print("\n" + availableNotes);
		availableNotes.addAll(collaboratedNote);
		return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, availableNotes, 200),
				HttpStatus.OK);
	}

	public ResponseEntity<Response> getAllArchiveNotesByUserId(String token) {
		Long userId = UserToken.getUserIdFromToken(token);
		try {
			List<Note> availableNotes = noteRepository.findAllNotesByUserId(userId, true, false);
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

	public ResponseEntity<Response> setIsTrash(Long noteId, boolean isTrash, String token) {
		Long userId = UserToken.getUserIdFromToken(token);
		Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new FundooNotesException(Message.NOTE_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST));
		if (note.getUser().getUserId().equals(userId)) {
			note.setTrash(isTrash);
			note.setModifiedDate(LocalDateTime.now());
			Note notes = noteRepository.save(note);
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, notes, 200), HttpStatus.OK);
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.INVALID_USER_ID, null, 404),
				HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<Response> deleteNote(Long noteId, String token) {
		Long userId = UserToken.getUserIdFromToken(token);
		Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new FundooNotesException(Message.NOTE_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST));
		if (note.getUser().getUserId().equals(userId)) {
			noteRepository.deleteById(noteId);
			return new ResponseEntity<Response>(
					userService.getResponse(Message.SUCCESSFUL, Message.NOTE_DELETED_SUCCESFULLY, 200), HttpStatus.OK);
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.INVALID_USER_ID, null, 404),
				HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<Response> updateNote(Note note, String token) {
		Long userId = UserToken.getUserIdFromToken(token);
		Note notes = noteRepository.findById(note.getNoteId())
				.orElseThrow(() -> new FundooNotesException(Message.NOTE_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST));
		if (notes.getUser().getUserId().equals(userId)) {
			notes.setTitle(note.getTitle());
			notes.setDescription(note.getDescription());
			notes.setModifiedDate(LocalDateTime.now());
			Note noteDetails = noteRepository.save(notes);
			return new ResponseEntity<Response>(
					userService.getResponse(Message.NOTE_UPDATED_SUCCESFULLY, noteDetails, 200), HttpStatus.OK);
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.INVALID_USER_ID, null, 404),
				HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<Response> updateColor(Long noteId, String color, String token) {
		Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new FundooNotesException(Message.NOTE_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST));
		Long userId = UserToken.getUserIdFromToken(token);
		if (userId == note.getUser().getUserId()) {
			note.setColor(color);
			note.setModifiedDate(LocalDateTime.now());
			note = noteRepository.save(note);
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_UPDATED_SUCCESFULLY, note, 200),
					HttpStatus.OK);
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.INVALID_USER_ID, null, 404),
				HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<Response> archiveNote(Long noteId, String token) {
		Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new FundooNotesException(Message.NOTE_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST));
		Long userId = UserToken.getUserIdFromToken(token);
		if (userId == note.getUser().getUserId()) {
			if (note.isArchive())
				note.setArchive(false);
			else
				note.setArchive(true);
			note.setModifiedDate(LocalDateTime.now());
			note = noteRepository.save(note);
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_UPDATED_SUCCESFULLY, note, 200),
					HttpStatus.OK);
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.INVALID_USER_ID, null, 404),
				HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<Response> pinNote(Long noteId, String token) {
		Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new FundooNotesException(Message.NOTE_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST));
		Long userId = UserToken.getUserIdFromToken(token);
		if (userId == note.getUser().getUserId()) {
			if (note.isPinned())
				note.setPinned(false);
			else
				note.setPinned(true);
			note.setModifiedDate(LocalDateTime.now());
			note = noteRepository.save(note);
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_UPDATED_SUCCESFULLY, note, 200),
					HttpStatus.OK);
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.INVALID_USER_ID, null, 404),
				HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<Response> addCollabrator(Collabrator collabrator, String token) {
		Long userId = UserToken.getUserIdFromToken(token);
		User user = userRepository.findByEmail(collabrator.getCollabratorEmail());
		if (user == null)
			throw new FundooNotesException(Message.EMAIL_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST);
		Note note = noteRepository.findById(collabrator.getNoteId())
				.orElseThrow(() -> new FundooNotesException(Message.NOTE_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST));
		if (userId == note.getUser().getUserId()) {
			user.getCollaboratedNote().add(note);
			note.getCollaboratedUser().add(user);
			note.setModifiedDate(LocalDateTime.now());
			userRepository.save(user);
			note = noteRepository.save(note);
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, note, 200), HttpStatus.OK);
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.INVALID_USER_ID, null, 404),
				HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<Response> removeCollabrator(Long noteId, String email, String token) {
		Long userId = UserToken.getUserIdFromToken(token);
		User user = userRepository.findByEmail(email);
		if (user == null)
			throw new FundooNotesException(Message.EMAIL_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST);
		Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new FundooNotesException(Message.NOTE_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST));
		if (userId == note.getUser().getUserId()) {
			user.getCollaboratedNote().remove(note);
			note.getCollaboratedUser().remove(user);
			userRepository.save(user);
			note = noteRepository.save(note);
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, note, 200), HttpStatus.OK);
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.INVALID_USER_ID, null, 404),
				HttpStatus.BAD_REQUEST);
	}

}
