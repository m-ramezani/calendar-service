package com.fobsolution.calendarservice.dto;

import com.sun.istack.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CreateEventDto implements Serializable {
    @NotNull
    private String title;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    private String originalEventId;
    @NotNull
    private String userId;
    private String color;
    private int numberOfRepetition;



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

    public int getNumberOfRepetition() {
        return numberOfRepetition;
    }

    public void setNumberOfRepetition(int numberOfRepetition) {
        this.numberOfRepetition = numberOfRepetition;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
                ", title='" + title + '\'' +
                ", startDateTime=" + start +
                ", endDateTime=" + end +
                ", originalEventId='" + originalEventId + '\'' +
                ", userId=" + userId +
                ", numberOfRepetition=" + numberOfRepetition +
                ", color=" + color +
                '}';
    }
}
