package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class Exception {
    private static final Logger log = LoggerFactory.getLogger(Exception.class);
    @ExceptionHandler(java.lang.Exception.class)
    public ResponseEntity<Object> exceptionHandler(HttpServletRequest req, HttpServletResponse res) {
        log.info("Has an error occur");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
