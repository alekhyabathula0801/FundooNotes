package com.bridgelabz.demo.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.model.Label;
import com.bridgelabz.demo.model.LabelNotesMapping;
import com.bridgelabz.demo.model.Note;
import com.bridgelabz.demo.model.Response;
import com.bridgelabz.demo.repository.LabelNotesMappingRepository;
import com.bridgelabz.demo.repository.LabelRepository;
import com.bridgelabz.demo.repository.NoteRepository;
import com.bridgelabz.demo.repository.UserRepository;

@Service
public class LabelService {

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private LabelRepository labelRepository;

	@Autowired
	private LabelNotesMappingRepository labelNotesMappingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	public LabelService() {

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
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.TRY_AGAIN_LATER, null, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> updateLabel(Label label) {
		try {
			userRepository.findById(label.getUserId()).get();
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		}
		try {
			if (labelRepository.findById(label.getId()).get() != null) {
				List<Label> allLabels = labelRepository.findAllByUserId(label.getUserId());
				if (allLabels.contains(label))
					return new ResponseEntity<Response>(userService.getResponse(Message.LABEL_NAME_EXISTS, null, 409),
							HttpStatus.CONFLICT);
				Label labels = labelRepository.save(label);
				return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, labels, 200),
						HttpStatus.OK);
			}
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.LABEL_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.TRY_AGAIN_LATER, null, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> deleteLabel(Long labelId) {
		try {
			if (labelNotesMappingRepository.findByLabelId(labelId) != null)
				labelNotesMappingRepository.deleteByLabelId(labelId);
			labelRepository.deleteById(labelId);
			return new ResponseEntity<Response>(
					userService.getResponse(Message.SUCCESSFUL, Message.LABEL_DELETED_SUCCESFULLY, 200), HttpStatus.OK);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.LABEL_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(userService.getResponse(Message.TRY_AGAIN_LATER, null, 500),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Response> getAllLabelsByUserId(Long userId) {
		try {
			userRepository.findById(userId).get();
			List<Label> allLabels = labelRepository.findAllByUserId(userId);
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

	public ResponseEntity<Response> mapLabelAndNotes(LabelNotesMapping labelNotesMapping) {
		Label label;
		try {
			label = labelRepository.findById(labelNotesMapping.getLabelId()).get();
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Response>(userService.getResponse(Message.LABEL_ID_DOESNOT_EXISTS, null, 404),
					HttpStatus.NOT_FOUND);
		}
		try {
			Note note = noteRepository.findById(labelNotesMapping.getNoteId()).get();
			if (!note.getUserId().equals(label.getUserId()))
				return new ResponseEntity<Response>(
						userService.getResponse(Message.USER_ID_OF_LABEL_DOESNT_MATCH_WITH_NOTE_USER_ID, null, 200),
						HttpStatus.OK);
			LabelNotesMapping labelNotesMappings = labelNotesMappingRepository.save(labelNotesMapping);
			return new ResponseEntity<Response>(userService.getResponse(Message.SUCCESSFUL, labelNotesMappings, 200),
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
