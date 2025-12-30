package com.ticketblitz.core.domain.event;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository {
    Optional<Event> findById(UUID id);
    Event save(Event event);
}