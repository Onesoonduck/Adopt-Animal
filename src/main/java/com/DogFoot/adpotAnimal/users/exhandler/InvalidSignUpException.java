package com.DogFoot.adpotAnimal.users.exhandler;

public class InvalidSignUpException extends RuntimeException {

    public InvalidSignUpException(String message) {
        super(message);
    }
}
