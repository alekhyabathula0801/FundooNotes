package com.bridgelabz.demo.exception;

import org.springframework.http.HttpStatus;

import com.bridgelabz.demo.enumeration.Message;

public class FundooNotesException extends RuntimeException {
	Message exceptionType;
	HttpStatus status;

	public FundooNotesException(Message exceptionType, HttpStatus status) {
		this.exceptionType = exceptionType;
		this.status = status;
	}
}
