package com.fobsolution.calendarservice.integeration;

import com.fobsolution.calendarservice.CalendarServiceApplication;
import com.fobsolution.calendarservice.dto.*;
import com.fobsolution.calendarservice.exception.EventNotFoundException;
import com.fobsolution.calendarservice.exception.EventOverlappingException;
import com.fobsolution.calendarservice.exception.EventTimeNotValidException;
import com.fobsolution.calendarservice.exception.UserNotFoundException;
import com.fobsolution.calendarservice.web.rest.EventController;
import com.fobsolution.calendarservice.web.rest.UserController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@SpringBootTest(classes = {CalendarServiceApplication.class})
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:config/application-test.yml")
@Rollback
public class EventTest {

    @Autowired
    private EventController eventController;
    @Autowired
    private UserController userController;

    private String userEmail;
    private String userPassword;

    @Before
    public void setup() {
        userEmail = "example_email@gmail.com";
        userPassword = "example_pass";
    }

    @Test
    @Transactional
    public void create_single_event() {
        UserDto userDto = registerUser();
        createSingleEvent(userDto.getId());
    }

    @Test
    @Transactional
    public void create_daily_event() {
        UserDto userDto = registerUser();
        CreateEventDto createEventDto = createEventDto();
        createEventDto.setUserId(userDto.getId().toString());
        createEventDto.setNumberOfRepetition(1);
        List<EventDto> eventDtos = eventController.create(createEventDto).getBody();
        assert eventDtos != null;
        assertNotNull(eventDtos.get(0).getId());
        assertNotNull(eventDtos.get(1).getId());
    }

    @Test(expected = EventTimeNotValidException.class)
    @Transactional
    public void create_when_times_invalid() {
        UserDto userDto = registerUser();
        CreateEventDto createEventDto = createEventDto();
        createEventDto.setUserId(userDto.getId().toString());
        createEventDto.setStart(createEventDto.getEnd());
        eventController.create(createEventDto).getBody();
    }

    @Test(expected = EventOverlappingException.class)
    @Transactional
    public void create_when_events_overlap() {
        UserDto userDto = registerUser();
        List<EventDto> eventDtos = createSingleEvent(userDto.getId());

        CreateEventDto createEventDto = createEventDto();
        createEventDto.setUserId(userDto.getId().toString());
        createEventDto.setStart(eventDtos.get(0).getStart().plusMinutes(10));
        createEventDto.setEnd(eventDtos.get(0).getEnd().plusHours(1));
        eventController.create(createEventDto);
    }

    @Test
    @Transactional
    public void update() {
        UserDto userDto = registerUser();
        List<EventDto> eventDtos = createSingleEvent(userDto.getId());

        UpdateEventDto updateEventDto = new UpdateEventDto();
        updateEventDto.setId(eventDtos.get(0).getId());
        updateEventDto.setTitle("New Title");
        updateEventDto.setStart(eventDtos.get(0).getStart());
        updateEventDto.setEnd(eventDtos.get(0).getEnd());
        updateEventDto.setUserId(userDto.getId().toString());
        List<EventDto> updateEventDtos = eventController.update(updateEventDto).getBody();
        assert updateEventDtos != null;
        assertEquals(updateEventDtos.get(0).getTitle(), "New Title");
    }

    @Test(expected = EventTimeNotValidException.class)
    @Transactional
    public void update_when_times_invalid() {
        UserDto userDto = registerUser();
        List<EventDto> eventDtos = createSingleEvent(userDto.getId());

        UpdateEventDto updateEventDto = new UpdateEventDto();
        updateEventDto.setId(eventDtos.get(0).getId());
        updateEventDto.setStart(eventDtos.get(0).getStart());
        updateEventDto.setEnd(eventDtos.get(0).getStart());
        updateEventDto.setUserId(userDto.getId().toString());
        eventController.update(updateEventDto);
    }

    @Test
    @Transactional
    public void update_event_and_following_events() {
        UserDto userDto = registerUser();
        CreateEventDto createEventDto = createEventDto();
        createEventDto.setUserId(userDto.getId().toString());
        createEventDto.setNumberOfRepetition(1);
        List<EventDto> eventDtos = eventController.create(createEventDto).getBody();
        assert eventDtos != null;
        assertNotNull(eventDtos.get(0).getId());
        assertNotNull(eventDtos.get(1).getId());

        UpdateEventDto updateEventDto = new UpdateEventDto();
        updateEventDto.setId(eventDtos.get(0).getId());
        updateEventDto.setTitle("New Title");
        updateEventDto.setStart(eventDtos.get(0).getStart());
        updateEventDto.setEnd(eventDtos.get(0).getEnd());
        updateEventDto.setUserId(userDto.getId().toString());
        updateEventDto.setUpdateFollowingEvents(true);
        List<EventDto> updateEventDtos = eventController.update(updateEventDto).getBody();
        assert updateEventDtos != null;
        assertEquals(updateEventDtos.get(0).getTitle(), "New Title");
        assertEquals(updateEventDtos.get(1).getTitle(), "New Title");

    }

    @Test
    @Transactional
    public void delete() {
        UserDto userDto = registerUser();
        List<EventDto> eventDtos = createSingleEvent(userDto.getId());
        DeleteEventDto deleteEventDto = new DeleteEventDto();
        deleteEventDto.setId(eventDtos.get(0).getId());
        eventController.delete(deleteEventDto);
    }

    @Test(expected = EventNotFoundException.class)
    public void delete_when_event_not_exists() {
        DeleteEventDto deleteEventDto = new DeleteEventDto();
        Random random = new Random();
        deleteEventDto.setId(random.nextLong());
        eventController.delete(deleteEventDto);
    }

    @Test
    @Transactional
    public void getByUser() {
        UserDto userDto = registerUser();
        createSingleEvent(userDto.getId());
        List<EventDto> fetchedEventDtos = eventController.getByUser(userDto.getId().toString()).getBody();
        assert fetchedEventDtos != null;
        assertNotNull(fetchedEventDtos.get(0).getId());
    }

    @Test(expected = UserNotFoundException.class)
    public void getByUser_WhenUserInvalid() {
        Random random = new Random();
        eventController.getByUser("39390034");
    }

    private CreateEventDto createEventDto() {
        CreateEventDto createEventDto = new CreateEventDto();
        createEventDto.setTitle("New Event");
        createEventDto.setStart(LocalDateTime.now());
        createEventDto.setEnd(LocalDateTime.now().plusHours(1));
        return createEventDto;
    }

    private UserDto registerUser() {
        CreateUserDto createUserDto = new CreateUserDto(userEmail, userPassword);
        UserDto userDto = userController.register(createUserDto).getBody();
        assert userDto != null;
        assertNotNull(userDto.getId());
        return userDto;
    }

    private List<EventDto> createSingleEvent(Long userId) {
        CreateEventDto createEventDto = createEventDto();
        createEventDto.setUserId(userId.toString());
        List<EventDto> eventDtos = eventController.create(createEventDto).getBody();
        assert eventDtos != null;
        assertNotNull(eventDtos.get(0).getId());
        return eventDtos;
    }

}
