package handler

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/manyloan/ticket-blitz-gateway/internal/queue"
)

type TicketRequest struct {
	EventID        string `json:"eventId" binding:"required"`
	UserID         string `json:"userId" binding:"required"`
	Quantity       int    `json:"quantity" binding:"required,gt=0"`
	IdempotencyKey string `json:"idempotencyKey" binding:"required"`
}

type TicketHandler struct {
	rabbitClient *queue.RabbitClient
}

func NewTicketHandler(client *queue.RabbitClient) *TicketHandler {
	return &TicketHandler{rabbitClient: client}
}

// Metodo que recebe o post e joga na fila
func (h *TicketHandler) BuyTicket(c *gin.Context) {
	var req TicketRequest

	// Valida o JSON
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	// Publica no RabbitMQ
	err := h.rabbitClient.Publish("ticket_purchase", req)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Erro ao processar solicitação"})
		return
	}

	// Resposta imediata
	c.JSON(http.StatusAccepted, gin.H{
		"message": "Solicitação recebida com sucesso. Estamos processando.",
		"status":  "PENDING",
	})
}
