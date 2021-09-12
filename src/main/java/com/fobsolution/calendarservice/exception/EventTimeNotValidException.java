package com.fobsolution.calendarservice.exception;

public class EventTimeNotValidException extends RuntimeException {
    public EventTimeNotValidException() {
    }

    public EventTimeNotValidException(String message) {
        super(message);
    }
}
