package com.semicolon.DigiBank.web.controllers.authController;

import com.semicolon.DigiBank.dtos.requests.LogInRequest;
import com.semicolon.DigiBank.dtos.responses.LogInResponse;
import com.semicolon.DigiBank.security.jwt.TokenProvider;
import com.semicolon.DigiBank.services.user.UserService;
import com.semicolon.DigiBank.web.exceptions.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogInRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getAccountNumber(),
                        request.getPin())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = tokenProvider.generateJWTToken(authentication);

        try {
            LogInResponse apiResponse = userService.logIn(request);
            apiResponse.setAccessToken(token);
            return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
