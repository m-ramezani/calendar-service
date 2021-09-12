package com.fobsolution.calendarservice.service;

import com.fobsolution.calendarservice.dto.CreateEventDto;
import com.fobsolution.calendarservice.dto.DeleteEventDto;
import com.fobsolution.calendarservice.dto.EventDto;
import com.fobsolution.calendarservice.dto.UpdateEventDto;
import com.fobsolution.calendarservice.exception.EventNotFoundException;
import com.fobsolution.calendarservice.exception.EventOverlappingException;
import com.fobsolution.calendarservice.exception.EventTimeNotValidException;
import com.fobsolution.calendarservice.exception.UserNotFoundException;

import java.util.List;

public interface EventService {
    List<EventDto> create(CreateEventDto createEventDto) throws UserNotFoundException, EventOverlappingException,
    EventTimeNotValidException;

    List<EventDto> update(UpdateEventDto updateEventDto) throws UserNotFoundException, EventNotFoundException, EventTimeNotValidException;

    void delete(DeleteEventDto deleteEventDto) throws EventNotFoundException;

    List<EventDto> getByUser(String userId) throws UserNotFoundException;
}
