package com.roczyno.projectservice.exception;


import com.roczyno.projectservice.util.ResponseHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.JDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptions {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptions.class);

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleValidationException(ConstraintViolationException ex) {
		logException(ex);
		String errorMessage = ex.getConstraintViolations().stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.joining("\n"));
		ErrorDetails errorDetails = new ErrorDetails(errorMessage, "Validation Error", LocalDateTime.now());
		return ResponseHandler.errorResponse(errorDetails, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(ProjectException.class)
	public ResponseEntity<Object> handleProjectException(ProjectException ex,WebRequest req){
		logException(ex);
		ErrorDetails err= new ErrorDetails(ex.getMessage(),req.getDescription(false),LocalDateTime.now());
		return ResponseHandler.errorResponse(err,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(JDBCException.class)
	public ResponseEntity<Object> handleJDBCException(JDBCException ex, WebRequest req) {
		logException(ex);
		List<ErrorDetails> errors = Collections.singletonList(
				new ErrorDetails(ex.getMessage(), req.getDescription(false), LocalDateTime.now())
		);
		return ResponseHandler.errorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
		logException(ex);
		List<ErrorDetails> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> new ErrorDetails(error.getDefaultMessage(), error.getField(), LocalDateTime.now()))
				.toList();
		return ResponseHandler.errorResponse(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest req) {
		logException(ex);
		List<ErrorDetails> errors = Collections.singletonList(
				new ErrorDetails(ex.getMessage(), req.getDescription(false), LocalDateTime.now())
		);
		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	private void logException(Exception ex) {
		logger.error("Exception: ", ex);
	}


}
