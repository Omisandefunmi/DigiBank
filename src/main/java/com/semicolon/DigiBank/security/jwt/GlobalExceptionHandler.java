package com.semicolon.DigiBank.security.jwt;

import com.semicolon.DigiBank.dtos.responses.AccountApiResponse;
import com.semicolon.DigiBank.web.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountNameAlreadyExistsException.class)
    public ResponseEntity<?> handleAccountNameAlreadyExistException(AccountNameAlreadyExistsException accountNameAlreadyExistsException){
        return new ResponseEntity<>(AccountApiResponse.builder()
                .message(accountNameAlreadyExistsException.getMessage())
                .statusCode(400)
                .success(false)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<?> handleAccountNotFoundException(AccountNotFoundException accountNotFoundException){
        return new ResponseEntity<>(AccountApiResponse.builder()
                .message(accountNotFoundException.getMessage())
                .statusCode(404)
                .success(false)
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DigiBankException.class)
    public ResponseEntity<?> handleDigiBankException(DigiBankException digiBankException){
        return new ResponseEntity<>(AccountApiResponse.builder()
                .message(digiBankException.getMessage())
                .statusCode(400)
                .success(false)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDetailsException.class)
    public ResponseEntity<?> handleInvalidDetailsException(InvalidDetailsException invalidDetailsException){
        return new ResponseEntity<>(AccountApiResponse.builder()
                .message(invalidDetailsException.getMessage())
                .statusCode(406)
                .success(false)
                .build(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UnsupportedDepositException.class)
    public ResponseEntity<?> handleUnsupportedDepositException(UnsupportedDepositException unsupportedDepositException){
        return new ResponseEntity<>(AccountApiResponse.builder()
                .message(unsupportedDepositException.getMessage())
                .statusCode(400)
                .success(false)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedWithdrawalException.class)
    public ResponseEntity<?> handleUnsupportedWithdrawalException(UnsupportedWithdrawalException unsupportedWithdrawalException){
        return new ResponseEntity<>(AccountApiResponse.builder()
                .message(unsupportedWithdrawalException.getMessage())
                .statusCode(400)
                .success(false)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<?> handleUserDoesNotExistException(UserDoesNotExistException userDoesNotExistException){
        return new ResponseEntity<>(AccountApiResponse.builder()
                .message(userDoesNotExistException.getMessage())
                .statusCode(404)
                .success(false)
                .build(), HttpStatus.NOT_FOUND);
    }

}
