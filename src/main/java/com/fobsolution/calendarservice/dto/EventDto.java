package com.fobsolution.calendarservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class EventDto implements Serializable {
    private Long id;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private String originalEventId;
    private String color;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getOriginalEventId() {
        return originalEventId;
    }

    public void setOriginalEventId(String originalEventId) {
        this.originalEventId = originalEventId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "EventDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startDateTime=" + start +
                ", endDateTime=" + end +
                ", originalEventId='" + originalEventId + '\'' +
                ", color=" + color +
                '}';
    }
}
