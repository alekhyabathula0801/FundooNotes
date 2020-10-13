package com.bridgelabz.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bridgelabz.demo.dto.Response;
import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.exception.FundooNotesException;
import com.bridgelabz.demo.model.Label;
import com.bridgelabz.demo.model.Note;
import com.bridgelabz.demo.model.User;
import com.bridgelabz.demo.repository.LabelRepository;
import com.bridgelabz.demo.repository.NoteRepository;
import com.bridgelabz.demo.repository.UserRepository;
import com.bridgelabz.demo.util.UserToken;

@Service
public class LabelService {

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private LabelRepository labelRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	public LabelService() {

	}

	public ResponseEntity<Response> addLabel(Label label, String token) {
		Long userId = UserToken.getUserIdFromToken(token);
		try {
			User user = userRepository.findById(userId).get();
			label.setUser(user);
			List<Label> allLabels = labelRepository.findAllLabelsByUserId(userId);
			if (allLabels.contains(label))
				return new ResponseEntity<Response>(userService.getResponse(Message.LABEL_NAME_EXISTS, null, 409),
						HttpStatus.CONFLICT);
			Label labels = labelRepository.save(label);
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, labels, 200),
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

	public ResponseEntity<Response> updateLabel(Label label, String token) {
		Long userId = UserToken.getUserIdFromToken(token);
		try {
			Label labels = labelRepository.findById(label.getLabelId()).get();
			if (labels.getUser().getUserId() == userId) {
				labels.setModifiedDate(LocalDateTime.now());
				labels.setLabelName(label.getLabelName());
				labels = labelRepository.save(labels);
				return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, labels, 200),
						HttpStatus.OK);
			}
			return new ResponseEntity<Response>(userService.getResponse(Message.INVALID_USER_ID, null, 404),
					HttpStatus.BAD_REQUEST);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.LABEL_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.TRY_AGAIN_LATER, null, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> deleteLabel(Long labelId, String token) {
		Long userId = UserToken.getUserIdFromToken(token);
		try {
			Label label = labelRepository.findById(labelId).get();
			if (label.getUser().getUserId().equals(userId)) {
				labelRepository.deleteById(labelId);
				return new ResponseEntity<Response>(
						userService.getResponse(Message.SUCCESSFUL, Message.LABEL_DELETED_SUCCESFULLY, 200),
						HttpStatus.OK);
			}
			return new ResponseEntity<Response>(userService.getResponse(Message.INVALID_USER_ID, null, 404),
					HttpStatus.BAD_REQUEST);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.LABEL_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.TRY_AGAIN_LATER, null, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> getAllLabelsByUserId(String token) {
		Long userId = UserToken.getUserIdFromToken(token);
		try {
			userRepository.findById(userId).get();
			List<Label> allLabels = labelRepository.findAllLabelsByUserId(userId);
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, allLabels, 200),
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

	public ResponseEntity<Response> mapLabelAndNotes(String token, Long labelId, Long noteId) {
		Label label;
		try {
			label = labelRepository.findById(labelId).get();
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.LABEL_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		}
		try {
			Note note = noteRepository.findById(noteId).get();
			if (!note.getUser().getUserId().equals(label.getUser().getUserId()))
				return new ResponseEntity<Response>(
						userService.getResponse(Message.USER_ID_OF_LABEL_DOESNT_MATCH_WITH_NOTE_USER_ID, null, 404),
						HttpStatus.BAD_REQUEST);
			note.getLabel().add(label);
			note.setModifiedDate(LocalDateTime.now());
			note = noteRepository.save(note);
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, note, 200), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.NOTE_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.TRY_AGAIN_LATER, null, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> removeLabelFromNote(String token, Long labelId, Long noteId) {
		Long userId = UserToken.getUserIdFromToken(token);
		Label label = labelRepository.findById(labelId)
				.orElseThrow(() -> new FundooNotesException(Message.LABEL_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST));
		Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new FundooNotesException(Message.NOTE_ID_DOESNOT_EXISTS, HttpStatus.BAD_REQUEST));
		if (label.getUser().getUserId() == userId) {
			note.getLabel().remove(label);
			note.setModifiedDate(LocalDateTime.now());
			note = noteRepository.save(note);
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, note, 200), HttpStatus.OK);
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.INVALID_USER_ID, null, 404),
				HttpStatus.BAD_REQUEST);
	}

}
