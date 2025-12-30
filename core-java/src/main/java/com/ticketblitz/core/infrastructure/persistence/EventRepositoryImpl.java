package com.ticketblitz.core.infrastructure.persistence;

import com.ticketblitz.core.domain.event.Event;
import com.ticketblitz.core.domain.event.EventRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class EventRepositoryImpl implements EventRepository {

    private final JpaEventRepository jpaRepository;

    public EventRepositoryImpl(JpaEventRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Event save(Event event) {
        return jpaRepository.save(event);
    }
}