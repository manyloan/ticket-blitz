package com.ticketblitz.core.application.dtos;

import java.util.UUID;

public record TicketPurchaseRequest(
    UUID eventId,
    UUID userId,
    int quantity,
    String idempotencyKey
) {}