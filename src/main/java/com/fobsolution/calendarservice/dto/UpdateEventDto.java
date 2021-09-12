package com.fobsolution.calendarservice.dto;

import com.sun.istack.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UpdateEventDto implements Serializable {
   @NotNull
   private Long id;
    @NotNull
    private String title;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @NotNull
    private String userId;
    private boolean updateFollowingEvents;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isUpdateFollowingEvents() {
        return updateFollowingEvents;
    }

    public void setUpdateFollowingEvents(boolean updateFollowingEvents) {
        this.updateFollowingEvents = updateFollowingEvents;
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
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", startDateTime=" + start +
                ", endDateTime=" + end +
                ", userId=" + userId +
                ", updateFollowingEvents=" + updateFollowingEvents +
                ", color=" + color +
                '}';
    }
}
