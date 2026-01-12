package com.luv2code.springboot.todos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ExceptionHandlers {

    private Clock clock;

    public ExceptionHandlers(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponses> handleException(ResponseStatusException exc) {
        return buildResponseEntity(exc, exc.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponses> handleException(Exception exc) {
        return buildResponseEntity(exc, BAD_REQUEST);
    }

    private ResponseEntity<ExceptionResponses> buildResponseEntity(Exception exc, HttpStatusCode status) {
        var error = new ExceptionResponses();
        error.setStatus(status.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(clock.millis());
        return new ResponseEntity<>(error, status);
    }
}
