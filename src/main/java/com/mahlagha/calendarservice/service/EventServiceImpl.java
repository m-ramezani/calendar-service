package com.mahlagha.calendarservice.service;

import com.mahlagha.calendarservice.dto.CreateEventDto;
import com.mahlagha.calendarservice.dto.DeleteEventDto;
import com.mahlagha.calendarservice.dto.EventDto;
import com.mahlagha.calendarservice.dto.UpdateEventDto;
import com.mahlagha.calendarservice.exception.EventNotFoundException;
import com.mahlagha.calendarservice.exception.EventOverlappingException;
import com.mahlagha.calendarservice.exception.EventTimeNotValidException;
import com.mahlagha.calendarservice.exception.UserNotFoundException;
import com.mahlagha.calendarservice.model.Event;
import com.mahlagha.calendarservice.model.User;
import com.mahlagha.calendarservice.repository.EventRepository;
import com.mahlagha.calendarservice.repository.UserRepository;
import com.mahlagha.calendarservice.validator.EventValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;
    private final UserRepository userRepository;

    public EventServiceImpl(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public List<EventDto> create(CreateEventDto createEventDto) throws UserNotFoundException, EventOverlappingException,
            EventTimeNotValidException {
        User user = userRepository.findById(Long.parseLong(createEventDto.getUserId()))
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist"));
        List<Event> events = new ArrayList<>();
        eventValidator.validateEventTime(createEventDto.getStart(), createEventDto.getEnd());
        if (eventValidator.eventOverlaps(null,createEventDto.getStart(), createEventDto.getEnd(), user.getId())) {
            throw new EventOverlappingException("This event overlaps with another event");
        }
        Event originalEvent = modelMapper.map(createEventDto, Event.class);
        originalEvent.setUser(user);
        originalEvent = eventRepository.save(originalEvent);
        events.add(originalEvent);

        if (createEventDto.getNumberOfRepetition() != 0) {
            for (int dayCount = 1; dayCount <= createEventDto.getNumberOfRepetition(); dayCount++) {
                LocalDateTime followingEventStart = createEventDto.getStart().plusDays(dayCount);
                LocalDateTime followingEventEnd = createEventDto.getEnd().plusDays(dayCount);
                if (!eventValidator.eventOverlaps(null,followingEventStart, followingEventEnd, user.getId())) {
                    Event followingEvent = modelMapper.map(createEventDto, Event.class);
                    followingEvent.setOriginalEventId(originalEvent.getId());
                    followingEvent.setStart(followingEventStart);
                    followingEvent.setEnd(followingEventEnd);
                    followingEvent.setUser(user);
                    eventRepository.save(followingEvent);
                    events.add(followingEvent);
                }
            }
        }
        return events.stream().map(event -> modelMapper.map(event, EventDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EventDto> update(UpdateEventDto updateEventDto) throws UserNotFoundException, EventNotFoundException,
            EventTimeNotValidException {
        User user = userRepository.findById(Long.parseLong(updateEventDto.getUserId()))
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist"));
        eventValidator.validateEventTime(updateEventDto.getStart(), updateEventDto.getEnd());
        Event originalEvent = eventRepository.findById(updateEventDto.getId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        List<EventDto> eventDtos = new ArrayList<>();

        if (!eventValidator.eventOverlaps(updateEventDto.getId(),updateEventDto.getStart(), updateEventDto.getEnd(), user.getId())) {
            originalEvent.setTitle(updateEventDto.getTitle());
            originalEvent.setStart(updateEventDto.getStart());
            originalEvent.setEnd(updateEventDto.getEnd());
            originalEvent.setColor(updateEventDto.getColor());
            eventRepository.save(originalEvent);
            eventDtos.add(modelMapper.map(originalEvent, EventDto.class));
        }

        if (updateEventDto.isUpdateFollowingEvents()) {
            long startTimeDiff = Duration.between(originalEvent.getStart(), updateEventDto.getStart()).toNanos();
            long endTimeDiff = Duration.between(originalEvent.getEnd(), updateEventDto.getEnd()).toNanos();
            List<Event> followingEvents = eventRepository.findByOriginalEventId(updateEventDto.getId());
            followingEvents.forEach(followingEvent -> {
                followingEvent.setStart(followingEvent.getStart().plusNanos(startTimeDiff));
                followingEvent.setEnd(followingEvent.getEnd().plusNanos(endTimeDiff));
                followingEvent.setTitle(updateEventDto.getTitle());
                followingEvent.setColor(updateEventDto.getColor());
                if (!eventValidator.eventOverlaps(updateEventDto.getId(),followingEvent.getStart(), followingEvent.getEnd(), user.getId())) {
                    eventRepository.save(followingEvent);
                    eventDtos.add(modelMapper.map(followingEvent, EventDto.class));
                }
            });
        }

        return eventDtos;
    }

    @Override
    @Transactional
    public void delete(DeleteEventDto deleteEventDto) throws EventNotFoundException {
        Event originalEvent = eventRepository.findById(deleteEventDto.getId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        if (deleteEventDto.isDeleteFollowingEvents()) {
            List<Event> followingEvents = eventRepository.findByOriginalEventId(deleteEventDto.getId());
            eventRepository.deleteAll(followingEvents);
        }
        eventRepository.delete(originalEvent);
    }

    @Override
    public List<EventDto> getByUser(String userId) throws UserNotFoundException {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist"));
        List<Event> events = eventRepository.findByUser(user);
        List<EventDto> eventDtos = events.stream().map(event -> modelMapper.map(event, EventDto.class)).collect(Collectors.toList());
        return eventDtos;
    }

}
