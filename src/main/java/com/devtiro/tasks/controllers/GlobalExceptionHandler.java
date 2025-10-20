package com.devtiro.tasks.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
/*
//import org.springframework.web.ErrorResponse;
* // ❌ Problem: The code showed an error because it imported Spring's org.springframework.web.ErrorResponse,
// which is an interface and cannot be instantiated using 'new'.

// ✅ Fix: Use your custom ErrorResponse record from com.devtiro.tasks.domain.dto instead of Spring's one.

// 💡 Tip: Always check imports when you have naming conflicts with classes like ErrorResponse or Response.

* */
import com.devtiro.tasks.domain.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage() == null ? "Bad request" : exception.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
       // https://www.notion.so/290b30e2930c8029bb45e025ff9c6ce4
    }


}
