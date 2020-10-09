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

import com.bridgelabz.demo.enumeration.Message;
import com.bridgelabz.demo.model.Label;
import com.bridgelabz.demo.model.LabelNotesMapping;
import com.bridgelabz.demo.model.Response;
import com.bridgelabz.demo.service.LabelService;
import com.bridgelabz.demo.service.UserService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(path = "fundoo")
public class LabelController {

	@Autowired
	UserService userService;

	@Autowired
	LabelService labelService;

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
		return labelService.addLabel(label);
	}

	@PutMapping(path = "/edit_label")
	@ApiOperation(value = "Edit label")
	public ResponseEntity<Response> updateLabel(@Valid @RequestBody Label label, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			return new ResponseEntity<Response>(userService.getResponse(Message.CONFLICT, errorMessages, 409),
					HttpStatus.CONFLICT);
		}
		return labelService.updateLabel(label);
	}

	@DeleteMapping(path = "/delete_label")
	@ApiOperation(value = "Delete label")
	public ResponseEntity<Response> deleteLabel(@RequestParam Long labelId) {
		if (labelId == null) {
			return new ResponseEntity<Response>(
					userService.getResponse(Message.CONFLICT, Message.LABEL_ID_CANNOT_BE_NULL, 409),
					HttpStatus.CONFLICT);
		}
		return labelService.deleteLabel(labelId);
	}

	@GetMapping(path = "/get_all_labels")
	@ApiOperation(value = "Get all labels by user id")
	public ResponseEntity<Response> getAllLabels(@RequestParam Long userId) {
		if (userId == null) {
			return new ResponseEntity<Response>(userService.getResponse(Message.USER_ID_CANNOT_BE_NULL, null, 409),
					HttpStatus.CONFLICT);
		}
		return labelService.getAllLabelsByUserId(userId);
	}

	@PostMapping(path = "/label_notes_mapping")
	@ApiOperation(value = "Map notes with label")
	public ResponseEntity<Response> mapLabelAndNotes(@Valid @RequestBody LabelNotesMapping labelNotesMapping,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<String>();
			for (ObjectError error : bindingResult.getAllErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			return new ResponseEntity<Response>(userService.getResponse(Message.CONFLICT, errorMessages, 409),
					HttpStatus.CONFLICT);
		}
		return labelService.mapLabelAndNotes(labelNotesMapping);
	}
}
