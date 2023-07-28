package org.Stasy.PublicPrivacyAppBackendHeroku.exception;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(String message){
        super(message);
    }

    public UserNotFoundException() {}
}
