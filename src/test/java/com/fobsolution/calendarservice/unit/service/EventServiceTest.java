package com.fobsolution.calendarservice.unit.service;

import com.fobsolution.calendarservice.dto.CreateEventDto;
import com.fobsolution.calendarservice.dto.DeleteEventDto;
import com.fobsolution.calendarservice.dto.EventDto;
import com.fobsolution.calendarservice.dto.UpdateEventDto;
import com.fobsolution.calendarservice.exception.EventNotFoundException;
import com.fobsolution.calendarservice.exception.EventTimeNotValidException;
import com.fobsolution.calendarservice.exception.UserNotFoundException;
import com.fobsolution.calendarservice.model.Event;
import com.fobsolution.calendarservice.model.User;
import com.fobsolution.calendarservice.repository.EventRepository;
import com.fobsolution.calendarservice.repository.UserRepository;
import com.fobsolution.calendarservice.service.EventServiceImpl;
import com.fobsolution.calendarservice.validator.EventValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {
    @InjectMocks
    EventServiceImpl eventService;

    @Mock
    UserRepository userRepository;

    @Mock
    EventRepository eventRepository;

    @Mock
    EventValidator eventValidator;

    @Mock
    ModelMapper modelMapper;


    @Before
    public void setup() {

    }

    @Test
    public void create() {
        CreateEventDto createEventDto = createEventDto();
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(eventValidator).validateEventTime(createEventDto.getStart(), createEventDto.getEnd());

        Event event = new Event();
        event.setTitle(createEventDto.getTitle());
        event.setStart(createEventDto.getStart());
        event.setEnd(createEventDto.getEnd());
        when(modelMapper.map(createEventDto, Event.class)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);

        EventDto eventDto = new EventDto();
        eventDto.setTitle(event.getTitle());
        eventDto.setStart(event.getStart());
        eventDto.setEnd(event.getEnd());

        when(modelMapper.map(event, EventDto.class)).thenReturn(eventDto);
        List<EventDto> eventDtos = eventService.create(createEventDto);
        assertEquals(eventDtos.get(0).getTitle(), createEventDto.getTitle());
    }

    @Test
    public void create_with_daily_repeat() {
        CreateEventDto createEventDto = createEventDto();
        createEventDto.setNumberOfRepetition(1);
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(eventValidator).validateEventTime(createEventDto.getStart(), createEventDto.getEnd());

        Event event = new Event();
        event.setTitle(createEventDto.getTitle());
        event.setStart(createEventDto.getStart());
        event.setEnd(createEventDto.getEnd());
        when(modelMapper.map(createEventDto, Event.class)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);

        EventDto eventDto = new EventDto();
        eventDto.setTitle(event.getTitle());
        eventDto.setStart(event.getStart());
        eventDto.setEnd(event.getEnd());

        when(modelMapper.map(event, EventDto.class)).thenReturn(eventDto);
        List<EventDto> eventDtos = eventService.create(createEventDto);
        assertEquals(eventDtos.get(0).getTitle(), createEventDto.getTitle());
        assertEquals(eventDtos.get(1).getTitle(), createEventDto.getTitle());
    }


    @Test(expected = UserNotFoundException.class)
    public void create_when_user_not_exists() {
        CreateEventDto createEventDto = createEventDto();
        when(userRepository.findById(anyLong())).thenThrow(UserNotFoundException.class);
        eventService.create(createEventDto);
    }

    @Test(expected = EventTimeNotValidException.class)
    public void create_when_event_time_invalid() {
        CreateEventDto createEventDto = createEventDto();
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        doThrow(EventTimeNotValidException.class).when(eventValidator).validateEventTime(createEventDto.getStart(), createEventDto.getEnd());
        eventService.create(createEventDto);
    }

    @Test
    public void update() {
        UpdateEventDto updateEventDto = createUpdateDto();
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Event event = new Event();
        event.setId(updateEventDto.getId());
        when(eventRepository.findById(updateEventDto.getId())).thenReturn(Optional.of(event));
        doNothing().when(eventValidator).validateEventTime(updateEventDto.getStart(), updateEventDto.getEnd());

        event.setTitle(updateEventDto.getTitle());
        event.setStart(updateEventDto.getStart());
        event.setEnd(updateEventDto.getEnd());
        when(eventRepository.save(event)).thenReturn(event);

        EventDto eventDto = new EventDto();
        eventDto.setTitle(event.getTitle());
        eventDto.setStart(event.getStart());
        eventDto.setEnd(event.getEnd());

        when(modelMapper.map(event, EventDto.class)).thenReturn(eventDto);
        List<EventDto> eventDtos = eventService.update(updateEventDto);

        assertEquals(eventDtos.get(0).getTitle(), updateEventDto.getTitle());
    }


    @Test
    public void update_with_following_events() {
        UpdateEventDto updateEventDto = createUpdateDto();
        updateEventDto.setUpdateFollowingEvents(true);
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Event event = new Event();
        event.setId(updateEventDto.getId());
        when(eventRepository.findById(updateEventDto.getId())).thenReturn(Optional.of(event));
        doNothing().when(eventValidator).validateEventTime(updateEventDto.getStart(), updateEventDto.getEnd());
        List<Event> events = new ArrayList<>();
        event.setTitle(updateEventDto.getTitle());
        event.setStart(updateEventDto.getStart());
        event.setEnd(updateEventDto.getEnd());
        when(eventRepository.save(event)).thenReturn(event);
        EventDto eventDto = new EventDto();
        eventDto.setTitle(event.getTitle());
        eventDto.setStart(event.getStart());
        eventDto.setEnd(event.getEnd());
        events.add(event);
        when(modelMapper.map(event, EventDto.class)).thenReturn(eventDto);
        when(eventRepository.findByOriginalEventId(anyLong())).thenReturn(events);

        List<EventDto> eventDtos = eventService.update(updateEventDto);

        assertEquals(eventDtos.get(0).getTitle(), updateEventDto.getTitle());
        assertEquals(eventDtos.get(1).getTitle(), updateEventDto.getTitle());
    }

    @Test(expected = UserNotFoundException.class)
    public void update_with_user_not_found() {
        UpdateEventDto updateEventDto = createUpdateDto();
        when(userRepository.findById(anyLong())).thenThrow(UserNotFoundException.class);
        eventService.update(updateEventDto);
    }

    @Test(expected = EventNotFoundException.class)
    public void update_with_event_not_found() {
        UpdateEventDto updateEventDto = createUpdateDto();
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(eventRepository.findById(updateEventDto.getId())).thenThrow(EventNotFoundException.class);
        eventService.update(updateEventDto);
    }

    @Test
    public void delete() {
        DeleteEventDto deleteEventDto = new DeleteEventDto();
        deleteEventDto.setId(1L);
        deleteEventDto.setDeleteFollowingEvents(false);

        Event event = new Event();
        event.setId(deleteEventDto.getId());
        when(eventRepository.findById(deleteEventDto.getId())).thenReturn(Optional.of(event));
        doNothing().when(eventRepository).delete(event);
        eventService.delete(deleteEventDto);
    }

    @Test
    public void delete_with_following() {
        DeleteEventDto deleteEventDto = new DeleteEventDto();
        deleteEventDto.setId(1L);
        deleteEventDto.setDeleteFollowingEvents(true);
        Event event = new Event();
        event.setId(deleteEventDto.getId());
        List<Event> events = new ArrayList<>();
        events.add(event);
        when(eventRepository.findById(deleteEventDto.getId())).thenReturn(Optional.of(event));
        when(eventRepository.findByOriginalEventId(deleteEventDto.getId())).thenReturn(events);
        doNothing().when(eventRepository).deleteAll(events);

        doNothing().when(eventRepository).delete(event);
        eventService.delete(deleteEventDto);
    }

    @Test(expected = EventNotFoundException.class)
    public void delete_when_event_not_found() {
        DeleteEventDto deleteEventDto = new DeleteEventDto();
        deleteEventDto.setId(1L);
        when(eventRepository.findById(deleteEventDto.getId())).thenThrow(EventNotFoundException.class);
        eventService.delete(deleteEventDto);
    }

    @Test
    public void getByUser() {
        User user = new User();
        user.setId(1L);
        List<Event> events = new ArrayList<>();
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Event Title");
        events.add(event);
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setTitle(event.getTitle());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findByUser(user)).thenReturn(events);
        when(modelMapper.map(event, EventDto.class)).thenReturn(eventDto);

        List<EventDto> eventDtos = eventService.getByUser("1");

        assertEquals(eventDtos.get(0).getTitle(), "Event Title");
    }

    @Test(expected = UserNotFoundException.class)
    public void getByUserWhenUserNotFound() {
        when(userRepository.findById(1L)).thenThrow(UserNotFoundException.class);
        List<EventDto> eventDtos = eventService.getByUser("1");
        assertEquals(eventDtos.get(0).getTitle(), "Event Title");
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
