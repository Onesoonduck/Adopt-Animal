package com.DogFoot.adpotAnimal.users.exhandler;

public class InvalidLoginException extends RuntimeException {

    public InvalidLoginException(String message) {
        super(message);
    }

}
