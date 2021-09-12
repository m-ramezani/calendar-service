package com.fobsolution.calendarservice.repository;

import com.fobsolution.calendarservice.model.Event;
import com.fobsolution.calendarservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUser(User user);

    List<Event> findByOriginalEventId(Long id);

    @Query("select event from Event event where " +
            "(:id is null or event.id <> :id) and " +
            "(event.originalEventId is null or event.originalEventId <> :id) and " +
            "event.user.id =:userId")
    List<Event> find(@Param("id") Long id, @Param("userId") Long userId);
}
