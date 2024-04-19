package com.macrace.pickleball.exceptions;

import com.macrace.pickleball.dto.response.ErrorResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", new Date());
        responseBody.put("status", status.value());

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        responseBody.put("errors", errors);
        log.info(responseBody.toString());
        return new ResponseEntity<>(responseBody, headers, status);
    }

    @ExceptionHandler(PhoneNumberExistException.class)
    public ResponseEntity<Object> handlePhoneNumberExistException(PhoneNumberExistException e) {
        log.error("PhoneNumberExistException {}", e.toString());
        return new ResponseEntity<>(new ErrorResponseTemplate(
                e.getErrorCode(),
                e.getMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PhoneNumberNotFoundException.class)
    public ResponseEntity<Object> handlePhoneNumberNotFoundException(PhoneNumberNotFoundException e) {
        log.error("PhoneNumberNotFoundException {}", e.toString());
        return new ResponseEntity<>(new ErrorResponseTemplate(
                e.getErrorCode(),
                e.getMessage()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameOrPasswordWrongException.class)
    public ResponseEntity<Object> handleUsernameOrPasswordWrongException(UsernameOrPasswordWrongException e) {
        log.error("UsernameOrPasswordWrongException {}", e.toString());
        return new ResponseEntity<>(new ErrorResponseTemplate(
                e.getErrorCode(),
                e.getMessage()
        ), HttpStatus.BAD_REQUEST);
    }
}
