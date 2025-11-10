package com.FinFlow.handler;

import com.FinFlow.dto.ResponseDTO;
import com.FinFlow.handler.ex.CustomApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @ExceptionHandler(CustomApiException.class)
  public ResponseEntity<?> apiException(CustomApiException customApiException) {
    log.error(customApiException.getMessage());
    return new ResponseEntity<>(new ResponseDTO<>(-1, customApiException.getMessage(), null), HttpStatus.BAD_REQUEST);
  }
}
