package com.bridgelabz.demo.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.bridgelabz.demo.dto.Response;
import com.bridgelabz.demo.enumeration.Message;

@ControllerAdvice
public class FundooNotesExceptionHandler {
	@Autowired
	Response response;

	/**
	 *
	 * @return Response Entity with message and status
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Response> handleMethodArgumentTypeMismatchException() {
		return getResponseEntity(Message.INVALID_INPUT, HttpStatus.BAD_REQUEST);
	}

	/**
	 *
	 * @return Response Entity with message and status
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Response> handleMessageNotReadableException() {
		return getResponseEntity(Message.INVALID_INPUT, HttpStatus.BAD_REQUEST);
	}

	/**
	 *
	 * @return Response Entity with message and status
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Response> handleNoHandlerFoundException() {
		return getResponseEntity(Message.METHOD_NOT_ALLOWED, HttpStatus.NOT_FOUND);
	}
	
	/**
	 *
	 * @return Response Entity with message and status
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Response> handleMethodNotAllowedException() {
		return getResponseEntity(Message.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED);
	}

	/**
	 *
	 * @return Response Entity with message and status
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> handleCustomException() {
		return getResponseEntity(Message.TRY_AGAIN_LATER, HttpStatus.NOT_FOUND);
	}

	/**
	 *
	 * @param message is Exception Message
	 * @param status  is Http status
	 * @return Response Entity with message and status
	 */
	public ResponseEntity<Response> getResponseEntity(Message message, HttpStatus status) {
		response.setMessage(message);
		response.setStatus(status.value());
		return new ResponseEntity<>(response, status);
	}
}
