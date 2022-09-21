package com.semicolon.DigiBank.web.controllers.authController;

import com.semicolon.DigiBank.dtos.requests.LogInRequest;
import com.semicolon.DigiBank.dtos.requests.SignUpRequest;
import com.semicolon.DigiBank.dtos.responses.LogInResponse;
import com.semicolon.DigiBank.security.jwt.TokenProvider;
import com.semicolon.DigiBank.services.user.UserService;
import com.semicolon.DigiBank.web.exceptions.AccountNameAlreadyExistsException;
import com.semicolon.DigiBank.web.exceptions.AccountNotFoundException;
import com.semicolon.DigiBank.web.exceptions.DigiBankException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @SneakyThrows
    @PostMapping("/create_account")
    public ResponseEntity<?> openAccount(@RequestBody SignUpRequest signUpRequest){
            var apiResponse = userService.signUp(signUpRequest);
            return  new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogInRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getAccountNumber(),
                        request.getPin())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = tokenProvider.generateJWTToken(authentication);

        return new ResponseEntity<>(new LogInResponse(true, token), HttpStatus.ACCEPTED);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }));
        return errors;
    }

}
