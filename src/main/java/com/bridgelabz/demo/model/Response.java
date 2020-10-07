package com.bridgelabz.demo.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bridgelabz.demo.enumeration.Message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@NoArgsConstructor
@Scope("prototype")
public class Response {
	private int status;
	private Message message;
	private Object result;

	public Response(Object result, Message message, int status) {
		this.result = result;
		this.message = message;
		this.status = status;
	}

}
