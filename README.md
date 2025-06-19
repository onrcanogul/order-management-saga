# Order Management Saga Microservices

## Project Overview

This project demonstrates a distributed microservices architecture for order and payment management, implementing the **Saga Pattern** to ensure eventual consistency across services. The system uses **Outbox/Inbox Pattern** and **Idempotent Tokens** to achieve reliable messaging and exactly-once message processing via RabbitMQ.

---

## Architecture Components

### Saga Orchestrator

- Coordinates long-running transactions between the Order and Payment services.
- Manages the state transitions and compensations based on the success or failure of each step.
- Orchestration logic is handled by dedicated Saga Manager components.

### Outbox/Inbox Pattern

- Outbox pattern ensures that outgoing messages are persisted within the same transaction as the business state change.
- Background publisher jobs read Outbox entries and publish them to RabbitMQ.
- Incoming messages are recorded in the Inbox table to prevent duplicate processing.
- Guarantees exactly-once processing across distributed systems.

### Idempotent Tokens

- Each event includes a unique Idempotent Token to identify and filter duplicate messages.
- Consumers verify the token to ensure that already processed messages are not handled again.
- Provides strong protection against message duplication during retries or failures.

---

## Microservices

### Order Service

- Handles order creation, updates, and management.
- Writes `OrderCreatedEvent` to the Outbox table upon order creation.
- Outbox Publisher reads from Outbox and publishes events to RabbitMQ.

### Payment Service

- Processes payment requests and generates payment result events.
- Writes `PaymentSucceededEvent` or `PaymentFailedEvent` to the Outbox table.
- Outbox Publisher sends payment events to RabbitMQ.

### Saga Orchestrator Service

- Listens to events and orchestrates the entire order-payment workflow using Saga Pattern.
- Writes processed event records into Inbox to prevent duplicate processing.
- Manages compensating actions if any service step fails.

---

## Technologies Used

- Spring Boot
- Maven
- Spring AMQP (RabbitMQ)
- Spring Data JPA (PostgreSQL)
- RabbitMQ
- Docker Compose (for local development)

---

## Reliable Messaging Architecture

- The Outbox/Inbox Pattern guarantees transactional consistency and reliable event delivery.
- Idempotent Token ensures duplicate messages are detected and ignored.
- Asynchronous messaging is handled via RabbitMQ to decouple service communication.

---

## Running the System

Each microservice runs independently. All services require RabbitMQ and PostgreSQL connections to function. Shared contracts, events, and message definitions are provided through the `common` module.


Upcoming feature -> DLQ

