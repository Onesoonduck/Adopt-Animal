package com.DogFoot.adpotAnimal.users.controller;

import com.DogFoot.adpotAnimal.users.exhandler.InvalidLoginException;
import com.DogFoot.adpotAnimal.users.exhandler.InvalidPasswordException;
import com.DogFoot.adpotAnimal.users.exhandler.InvalidSignUpException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class UsersExeceptionHandler {

    // ID, Email 중복예외
    @ExceptionHandler(InvalidSignUpException.class)
    public ResponseEntity handleInvalidSignUpException(InvalidSignUpException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    // Password 예외
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity handleInvalidPasswordException(InvalidPasswordException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    // Login 예외
    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity handleInvalidLoginException(InvalidLoginException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
