package com.mahlagha.calendarservice.web.rest;

import com.mahlagha.calendarservice.dto.DeleteEventDto;
import com.mahlagha.calendarservice.dto.EventDto;
import com.mahlagha.calendarservice.dto.UpdateEventDto;
import com.mahlagha.calendarservice.exception.EventNotFoundException;
import com.mahlagha.calendarservice.exception.EventTimeNotValidException;
import com.mahlagha.calendarservice.exception.UserNotFoundException;
import com.mahlagha.calendarservice.service.EventService;
import com.mahlagha.calendarservice.dto.CreateEventDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event/")
@CrossOrigin
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @PostMapping("create")
    public ResponseEntity<List<EventDto>> create(@RequestBody CreateEventDto createEventDto) {
        return ResponseEntity.ok(eventService.create(createEventDto));
    }

    @PutMapping("update")
    public ResponseEntity<List<EventDto>> update(@RequestBody UpdateEventDto updateEventDto) throws UserNotFoundException, EventNotFoundException,
            EventTimeNotValidException {
        return ResponseEntity.ok(eventService.update(updateEventDto));
    }

    @DeleteMapping("delete")
    public ResponseEntity<HttpStatus> delete(@RequestBody DeleteEventDto deleteEventDto) throws EventNotFoundException {
        eventService.delete(deleteEventDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<EventDto>> getByUser(@PathVariable String userId) throws UserNotFoundException{
        return ResponseEntity.ok(eventService.getByUser(userId));
    }

}
