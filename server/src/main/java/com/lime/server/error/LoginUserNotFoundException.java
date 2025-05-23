package com.lime.server.error;

public class LoginUserNotFoundException extends Exception {
    public LoginUserNotFoundException() {
    }

    public LoginUserNotFoundException(String message) {
        super(message);
    }
}
