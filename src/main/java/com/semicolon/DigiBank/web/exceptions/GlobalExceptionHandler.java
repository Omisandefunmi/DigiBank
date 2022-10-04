package com.semicolon.DigiBank.web.exceptions;

import com.semicolon.DigiBank.dtos.responses.AccountApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AccountNameAlreadyExistsException.class)
    public ResponseEntity<?> handleAccountNameAlreadyExistsException(AccountNameAlreadyExistsException accountNameAlreadyExistsException) {
        AccountApiResponse apiResponse = AccountApiResponse.builder()
                .success(false)
                .message(accountNameAlreadyExistsException.getMessage())
                .statusCode(406)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<?> handleAccountNotFoundException(AccountNotFoundException accountNotFoundException) {
        AccountApiResponse apiResponse = AccountApiResponse.builder()
                .success(false)
                .message(accountNotFoundException.getMessage())
                .statusCode(404)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDetailsException.class)
    public ResponseEntity<?> handleInvalidDetailsException(InvalidDetailsException invalidDetailsException) {
        AccountApiResponse apiResponse = AccountApiResponse.builder()
                .success(false)
                .message(invalidDetailsException.getMessage())
                .statusCode(400)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedDepositException.class)
    public ResponseEntity<?> handleUnsupportedDepositException(UnsupportedDepositException unsupportedDepositException) {
        AccountApiResponse apiResponse = AccountApiResponse.builder()
                .success(false)
                .message(unsupportedDepositException.getMessage())
                .statusCode(406)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UnsupportedWithdrawalException.class)
    public ResponseEntity<?> handleUnsupportedWithdrawalException(UnsupportedWithdrawalException unsupportedDepositException) {
        AccountApiResponse apiResponse = AccountApiResponse.builder()
                .success(false)
                .message(unsupportedDepositException.getMessage())
                .statusCode(406)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<?> handleUnsupportedWithdrawalException(UserDoesNotExistException userDoesNotExistException) {
        AccountApiResponse apiResponse = AccountApiResponse.builder()
                .success(false)
                .message(userDoesNotExistException.getMessage())
                .statusCode(406)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
    }
    }
