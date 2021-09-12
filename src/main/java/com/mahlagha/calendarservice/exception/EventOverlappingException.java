package com.mahlagha.calendarservice.exception;

public class EventOverlappingException extends RuntimeException {
    public EventOverlappingException() {
    }

    public EventOverlappingException(String message) {
        super(message);
    }
}
