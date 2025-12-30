package main

import (
	"log"

	"github.com/gin-gonic/gin"
	"github.com/manyloan/ticket-blitz-gateway/internal/handler"
	"github.com/manyloan/ticket-blitz-gateway/internal/queue"
)

func main() {
	// Conexao com RabbitMq
	rabbitClient, err := queue.Connect()
	if err != nil {
		log.Fatalf("Erro ao conectar ao RabbitMQ: %v", err)
	}
	defer rabbitClient.Close()

	// Configurar Handler
	ticketHandler := handler.NewTicketHandler(rabbitClient)

	// Configurar servidor web
	r := gin.Default()

	// route de compra
	r.POST("/buy", ticketHandler.BuyTicket)

	log.Println("🚀 Gateway rodando na porta 8081")
	r.Run(":8081")
}
