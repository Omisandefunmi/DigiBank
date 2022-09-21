package com.semicolon.DigiBank.services.user;


import com.semicolon.DigiBank.dtos.requests.*;

import com.semicolon.DigiBank.dtos.responses.SignUpResponse;
import com.semicolon.DigiBank.web.exceptions.*;


public interface UserService {
    SignUpResponse signUp(SignUpRequest signUpRequest) throws DigiBankException, AccountNameAlreadyExistsException;

}
