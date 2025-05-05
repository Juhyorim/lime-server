package com.lime.server.busApi.error;

public class BusAPIException extends RuntimeException {
    public BusAPIException() {
        super();
    }

    public BusAPIException(String message) {
        super(message);
    }
}
