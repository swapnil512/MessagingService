package com.opentext.interview.messagingsys;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Class that implements a handler of exceptions and errors in the API, using
 * {@ControllerAdvice} and sending the proper response to the client.
 * 
 * Currently we handled only the IOException, but it can be easily extended to
 * handle user defined exceptions as well.
 * 
 * @author swapn
 *
 */
@ControllerAdvice
@RequestMapping(produces = "application/vnd.error+json")
public class MessagesControllerAdvice<T> {

	@ExceptionHandler(IOException.class)
	public ResponseEntity<Errors> assertionException(final IOException exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
