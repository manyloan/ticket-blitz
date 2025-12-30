package com.ticketblitz.core.infrastructure.persistence;

import com.ticketblitz.core.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

// Interface mágica do Spring Data
public interface JpaEventRepository extends JpaRepository<Event, UUID> {
}