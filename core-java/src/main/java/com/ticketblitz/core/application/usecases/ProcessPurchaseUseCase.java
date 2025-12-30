package com.ticketblitz.core.application.usecases;

import com.ticketblitz.core.application.dtos.TicketPurchaseRequest;
import com.ticketblitz.core.domain.event.Event;
import com.ticketblitz.core.domain.event.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessPurchaseUseCase {
    private final EventRepository eventRepository;

    public ProcessPurchaseUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional // Para garantir atomicidade (Tudou ou Nada)
    public void execute(TicketPurchaseRequest request) {
        // 1. Busca o evento (Ou lança erro se não existir)
        Event event = eventRepository.findById(request.eventId())
            .orElseThrow(() -> new IllegalArgumentException("Event not found: " + request.eventId()));

        // 2. Tenta diminuir o estoque (Regra de Negócio no Domínio)
        boolean success = event.decreaseAvailability(request.quantity());

        if (!success) {
            throw new IllegalArgumentException("Sold out! No tickets available for event: " + event.getTitle());
        }

        // 3. Persiste a mudança
        // Aqui o Hibernate vai checar a versão (@Version). 
        // Se alguém mudou o registro antes de nós, ele lança OptimisticLockingFailureException
        eventRepository.save(event);

        System.out.println("✅ Compra processada com sucesso para User: " + request.userId());

        // TODO: Futuramente aqui criaremos a entidade "Order" e "Ticket"
    }
}