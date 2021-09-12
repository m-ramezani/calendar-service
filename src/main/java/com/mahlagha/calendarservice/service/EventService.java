package com.mahlagha.calendarservice.service;

import com.mahlagha.calendarservice.dto.CreateEventDto;
import com.mahlagha.calendarservice.dto.DeleteEventDto;
import com.mahlagha.calendarservice.dto.EventDto;
import com.mahlagha.calendarservice.dto.UpdateEventDto;
import com.mahlagha.calendarservice.exception.EventNotFoundException;
import com.mahlagha.calendarservice.exception.EventOverlappingException;
import com.mahlagha.calendarservice.exception.EventTimeNotValidException;
import com.mahlagha.calendarservice.exception.UserNotFoundException;

import java.util.List;

public interface EventService {
    List<EventDto> create(CreateEventDto createEventDto) throws UserNotFoundException, EventOverlappingException,
            EventTimeNotValidException;

    List<EventDto> update(UpdateEventDto updateEventDto) throws UserNotFoundException, EventNotFoundException, EventTimeNotValidException;

    void delete(DeleteEventDto deleteEventDto) throws EventNotFoundException;

    List<EventDto> getByUser(String userId) throws UserNotFoundException;
}
