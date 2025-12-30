package com.ticketblitz.core.infrastructure.messaging;

import com.ticketblitz.core.application.dtos.TicketPurchaseRequest;
import com.ticketblitz.core.application.usecases.ProcessPurchaseUseCase;
import com.ticketblitz.core.infrastructure.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TicketPurchaseListener {
    private final ProcessPurchaseUseCase processPurchaseUseCase;

    // Injecao do caso de uso
    public TicketPurchaseListener(ProcessPurchaseUseCase processPurchaseUseCase) {
        this.processPurchaseUseCase = processPurchaseUseCase;
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void onTicketPurchaseReceived(TicketPurchaseRequest request){
        System.out.println("Mensagem recebida da fila: " + request);

        try {
            // Chama a Application
            processPurchaseUseCase.execute(request);
        } catch (Exception e) {
            // TODO enviar para uma fila DLQ
            System.err.println("Erro ao processar compra: " + e.getMessage());
        }
    }
}