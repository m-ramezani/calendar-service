package com.mahlagha.calendarservice.validator;

import com.mahlagha.calendarservice.exception.EventTimeNotValidException;
import com.mahlagha.calendarservice.model.Event;
import com.mahlagha.calendarservice.repository.EventRepository;
import com.mahlagha.calendarservice.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class EventValidator {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public EventValidator(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public void validateEventTime(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || end.equals(start)) {
            throw new EventTimeNotValidException("End time must be greater than start time");
        }
    }

    public boolean eventOverlaps(Long eventId, LocalDateTime start, LocalDateTime end, Long userId) {
        List<Event> events = eventRepository.find(eventId, userId);
        Optional<Event> optionalEvent = events.stream().filter(event -> dateConflicts(start, end, event.getStart(),
                event.getEnd())).findFirst();
        return optionalEvent.isPresent();

    }

    private boolean dateConflicts(LocalDateTime start, LocalDateTime end, LocalDateTime existingStart, LocalDateTime existingEnd) {
        return ((start.isAfter(existingStart) && start.isBefore(existingEnd) || start.equals(existingStart) || start.equals(existingEnd))) ||
                ((end.isAfter(existingStart) && end.isBefore(existingEnd)) || end.equals(existingEnd) || end.equals(existingStart));
    }


}
