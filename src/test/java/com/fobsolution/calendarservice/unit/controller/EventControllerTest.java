package com.fobsolution.calendarservice.unit.controller;

import com.fobsolution.calendarservice.dto.CreateEventDto;
import com.fobsolution.calendarservice.dto.DeleteEventDto;
import com.fobsolution.calendarservice.dto.EventDto;
import com.fobsolution.calendarservice.dto.UpdateEventDto;
import com.fobsolution.calendarservice.service.EventService;
import com.fobsolution.calendarservice.web.rest.EventController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventControllerTest {
    @InjectMocks
    EventController eventController;

    @Mock
    EventService eventService;

    @Before
    public void setup() {
    }

    @Test
    public void create() {
        CreateEventDto createEventDto = createEventDto();
        List<EventDto> eventDtos = new ArrayList<>();
        EventDto eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setTitle(createEventDto.getTitle());
        eventDtos.add(eventDto);
        when(eventService.create(createEventDto)).thenReturn(eventDtos);
        List<EventDto> result = eventController.create(createEventDto).getBody();
        assertEquals(result.get(0).getTitle(), createEventDto.getTitle());
    }

    @Test
    public void update() {
        UpdateEventDto updateEventDto = createUpdateDto();
        List<EventDto> eventDtos = new ArrayList<>();
        EventDto eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setTitle(updateEventDto.getTitle());
        eventDtos.add(eventDto);
        when(eventService.update(updateEventDto)).thenReturn(eventDtos);
        List<EventDto> result = eventController.update(updateEventDto).getBody();
        assertEquals(result.get(0).getTitle(), updateEventDto.getTitle());
    }

    @Test
    public void delete() {
        DeleteEventDto deleteEventDto = new DeleteEventDto();
        deleteEventDto.setId(1L);
        deleteEventDto.setDeleteFollowingEvents(false);
        doNothing().when(eventService).delete(deleteEventDto);
        eventController.delete(deleteEventDto);
    }

    @Test
    public void getByUser() {
        List<EventDto> eventDtos = new ArrayList<>();
        EventDto eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setTitle("Event Title");
        eventDtos.add(eventDto);
        when(eventService.getByUser("1")).thenReturn(eventDtos);
        List<EventDto> result = eventController.getByUser("1").getBody();
        assertEquals(result.get(0).getTitle(), "Event Title");
    }


    private CreateEventDto createEventDto() {
        CreateEventDto createEventDto = new CreateEventDto();
        createEventDto.setTitle("New Event");
        createEventDto.setStart(LocalDateTime.now());
        createEventDto.setEnd(LocalDateTime.now().plusHours(1));
        createEventDto.setUserId("1");
        createEventDto.setNumberOfRepetition(0);
        return createEventDto;
    }

    private UpdateEventDto createUpdateDto() {
        UpdateEventDto updateEventDto = new UpdateEventDto();
        updateEventDto.setId(1L);
        updateEventDto.setTitle("Edit Title");
        updateEventDto.setStart(LocalDateTime.now());
        updateEventDto.setEnd(LocalDateTime.now().plusHours(1));
        updateEventDto.setUpdateFollowingEvents(false);
        updateEventDto.setUserId("1");

        return updateEventDto;
    }

}
