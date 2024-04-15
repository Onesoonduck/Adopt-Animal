package com.DogFoot.adpotAnimal.users.exhandler;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
