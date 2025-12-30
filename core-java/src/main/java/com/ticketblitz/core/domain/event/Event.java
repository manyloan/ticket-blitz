package com.ticketblitz.core.domain.event;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@NoArgsConstructor // Exigido pelo JPA
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int totalCapacity;

    @Column(nullable = false)
    private int availableTickets;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime date;

    @Version // O SEGREDO DO LOCK OTIMISTA
    private Long version;

    // Construtor rico para garantir consistência
    public Event(String title, int totalCapacity, BigDecimal price, LocalDateTime date) {
        if (totalCapacity <= 0) throw new IllegalArgumentException("Capacity must be greater than zero");
        if (price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Price cannot be negative");
        
        this.title = title;
        this.totalCapacity = totalCapacity;
        this.availableTickets = totalCapacity; // Começa cheio
        this.price = price;
        this.date = date;
    }

    // Lógica de Domínio (Rich Domain Model)
    // Retorna boolean para indicar sucesso, sem lançar exception (cleaner control flow)
    public boolean decreaseAvailability(int quantity) {
        if (this.availableTickets >= quantity) {
            this.availableTickets -= quantity;
            return true;
        }
        return false;
    }
}