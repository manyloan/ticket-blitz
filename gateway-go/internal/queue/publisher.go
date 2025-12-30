package queue

import (
	"context"
	"encoding/json"
	"log"
	"time"

	amqp "github.com/rabbitmq/amqp091-go"
)

type RabbitClient struct {
	conn    *amqp.Connection
	channel *amqp.Channel
}

// Conectar ao RabbitMq e preparar canal
func Connect() (*RabbitClient, error) {
	conn, err := amqp.Dial("amqp://user:password@localhost:5672/")
	if err != nil {
		return nil, err
	}

	ch, err := conn.Channel()
	if err != nil {
		return nil, err
	}

	return &RabbitClient{
		conn:    conn,
		channel: ch,
	}, nil
}

// Publish para enviar mensagem para Exchange
func (client *RabbitClient) Publish(eventName string, payload interface{}) error {
	body, err := json.Marshal(payload)
	if err != nil {
		return err
	}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	err = client.channel.PublishWithContext(ctx,
		"ticket.exchange", // Exchange (Tem que ser o mesmo nome do Java)
		"ticket.purchase", // Routing Key (Tem que ser a mesma do Java)
		false,
		false,
		amqp.Publishing{
			ContentType: "application/json",
			Body:        body,
		})
	if err != nil {
		log.Printf("❌ Falha ao publicar: %v", err)
		return err
	}

	log.Println("✅ Mensagem enviada para a fila!")
	return nil
}

// Fechar as conexoes
func (client *RabbitClient) Close() {
	client.channel.Close()
	client.conn.Close()
}
