package com.fobsolution.calendarservice.dto;

import com.sun.istack.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DeleteEventDto implements Serializable {
   @NotNull
   private Long id;
   private boolean deleteFollowingEvents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDeleteFollowingEvents() {
        return deleteFollowingEvents;
    }

    public void setDeleteFollowingEvents(boolean deleteFollowingEvents) {
        this.deleteFollowingEvents = deleteFollowingEvents;
    }

    @Override
    public String toString() {
        return "DeleteEventDto{" +
                "id=" + id +
                ", deleteFollowingEvents=" + deleteFollowingEvents +
                '}';
    }
}
