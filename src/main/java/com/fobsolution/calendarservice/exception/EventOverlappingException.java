package com.fobsolution.calendarservice.exception;

public class EventOverlappingException extends RuntimeException {
    public EventOverlappingException() {
    }

    public EventOverlappingException(String message) {
        super(message);
    }
}
