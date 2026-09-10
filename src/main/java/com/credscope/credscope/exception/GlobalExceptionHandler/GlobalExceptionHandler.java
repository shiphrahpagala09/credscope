package com.credscope.credscope.exception.GlobalExceptionHandler;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	 @ExceptionHandler(RuntimeException.class)
	    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
	        return ResponseEntity.badRequest().body(ex.getMessage());
	    }

	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
	        String message = ex.getBindingResult().getFieldErrors().stream()
	                .map(err -> err.getField() + ": " + err.getDefaultMessage())
	                .collect(Collectors.joining(", "));
	        return ResponseEntity.badRequest().body(message);
	    }

}
