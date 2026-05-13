# 🚀 Quick Commerce System (Microservices)

A distributed backend system simulating a quick commerce platform (like Blinkit / Swiggy Instamart), built using modern microservices architecture with focus on scalability, resilience, observability, and distributed system design principles.

---

# 🧠 Overview

This project demonstrates how production-grade backend systems are designed using:

- Microservices architecture
- Distributed communication patterns
- Event-driven systems using Kafka
- Service discovery
- API Gateway routing
- Fault tolerance & resilience
- Monitoring & centralized logging
- Containerized infrastructure

---

# 🏗️ System Architecture

```text
                    ┌──────────────────┐
                    │      Client      │
                    └────────┬─────────┘
                             │
                             ▼
                 ┌─────────────────────┐
                 │    API Gateway      │
                 │  (Spring Gateway)   │
                 └────────┬────────────┘
                          │
          ┌───────────────┴────────────────┐
          ▼                                ▼
┌──────────────────┐             ┌──────────────────┐
│  Order Service   │◄──────────►│ Inventory Service │
└────────┬─────────┘   Feign     └──────────────────┘
         │
         ▼
┌──────────────────┐
│      Kafka       │
└──────────────────┘

         ▲
         │
┌──────────────────┐
│ Discovery Server │
│     (Eureka)     │
└──────────────────┘
```

---

# ⚙️ Tech Stack

## Backend
- Java 21
- Spring Boot 3
- Spring Cloud
- Spring Data JPA
- Spring Cloud Gateway
- OpenFeign
- Resilience4j

---

## Messaging
- Apache Kafka
- Zookeeper

---

## Database
- PostgreSQL

---

## Infrastructure & DevOps
- Docker
- Docker Compose

---

## Monitoring & Observability
- Spring Boot Actuator
- Prometheus
- Grafana
- Loki
- Promtail

---

# 🧩 Microservices

## 🧾 Order Service

Responsible for:
- order creation
- inventory validation
- event publishing
- retry/fallback handling

### Features
- OpenFeign communication
- Circuit Breaker
- Retry mechanism
- Fallback handling
- Kafka producer
- PostgreSQL persistence

---

## 📦 Inventory Service

Responsible for:
- inventory management
- stock validation
- inventory event handling

### Features
- inventory APIs
- stock validation
- Kafka consumer
- PostgreSQL persistence

---

## 🌐 API Gateway

Built using Spring Cloud Gateway.

### Features
- centralized routing
- single entry point
- service discovery integration
- scalable entry point for clients

---

## 🔍 Discovery Server (Eureka)

Responsible for:
- service registration
- service discovery
- dynamic routing support

---

# 🔥 Features Implemented

## ✅ Microservices Architecture
- independently deployable services
- service isolation
- distributed communication

---

## ✅ API Gateway
- centralized routing
- single entry point
- scalable API access

---

## ✅ Service Discovery
- Eureka-based dynamic registration
- automatic service lookup

---

## ✅ OpenFeign Communication
- synchronous inter-service REST communication
- declarative HTTP clients

---

## ✅ Fault Tolerance & Resilience
Implemented using Resilience4j:
- Circuit Breaker
- Retry
- Fallback handling

---

## ✅ Kafka Event Streaming
- asynchronous event-driven communication
- order event publishing
- inventory event consumption

---

## ✅ Dockerized Infrastructure
Containerized services:
- PostgreSQL
- Kafka
- Zookeeper
- Gateway
- Eureka
- Microservices

---

## ✅ Monitoring & Observability

### Spring Boot Actuator
- health checks
- metrics
- circuit breaker metrics

### Prometheus
- metrics scraping
- monitoring

### Grafana
- real-time dashboards
- JVM metrics
- HTTP metrics
- resilience metrics

### Loki + Promtail
- centralized log aggregation
- live log streaming
- container log monitoring

---

# 📊 Observability Stack

```text
                 ┌────────────┐
                 │ Prometheus │
                 └─────┬──────┘
                       │
                 ┌─────▼──────┐
                 │  Grafana   │
                 └─────┬──────┘
                       │
          ┌────────────┴────────────┐
          ▼                         ▼
   Metrics Monitoring        Centralized Logs
                                    │
                               ┌────▼────┐
                               │  Loki   │
                               └────┬────┘
                                    │
                               ┌────▼────┐
                               │Promtail │
                               └─────────┘
```

---

# 🐳 Infrastructure Setup

Services running via Docker Compose:

- PostgreSQL
- Kafka
- Zookeeper
- Eureka Discovery Server
- API Gateway
- Order Service
- Inventory Service
- Prometheus
- Grafana
- Loki
- Promtail

---

# ▶️ How to Run

## 1. Clone Repository

```bash
git clone <repo-url>
cd quick-commerce-system
```

---

## 2. Build Services

```bash
mvn clean install -DskipTests
```

---

## 3. Start Entire System

```bash
docker compose up -d
```

---

# 🌐 Service URLs

| Service | URL |
|---|---|
| Gateway | http://localhost:8080 |
| Order Service | http://localhost:8081 |
| Inventory Service | http://localhost:8082 |
| Eureka Dashboard | http://localhost:8761 |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 |

---

# 🧪 API Testing

## Add Inventory

```http
POST /inventory
```

Request Body:

```json
{
  "productId": 1,
  "availableQuantity": 10
}
```

---

## Check Inventory

```http
GET /inventory/check?productId=1&quantity=5
```

---

## Create Order

```http
POST /orders
```

Request Body:

```json
{
  "productId": 1,
  "quantity": 2
}
```

---

# 📈 Monitoring Dashboards

Implemented dashboards for:
- JVM memory usage
- HTTP request metrics
- Circuit breaker metrics
- CPU usage
- Live threads
- Centralized logs

---

# 🔐 Upcoming Enhancements

## 🚀 Security
- Spring Security
- JWT Authentication
- Role-based authorization
- Gateway authentication filter

---

## 🚀 Additional Microservices
- User Service
- Payment Service
- Notification Service
- Product Catalog Service

---

## 🚀 Advanced Infrastructure
- Redis caching
- Kubernetes deployment
- CI/CD pipelines
- GitHub Actions
- Jenkins

---

## 🚀 Advanced Distributed Systems
- Saga Pattern
- CQRS
- Event Sourcing
- Distributed tracing
- OpenTelemetry

---

# 🧠 Learning Outcomes

This project focuses on production-grade backend engineering concepts:

- Microservices communication
- Distributed system design
- Event-driven architecture
- Fault tolerance & resilience
- Monitoring & observability
- Centralized logging
- Containerized infrastructure
- Scalable backend architecture

---

# 👨‍💻 Author

Swapnil Kashish  
Backend Engineer | Java | Spring Boot | Microservices

---

# ⭐ If you like this project

Give it a ⭐ on GitHub and follow for more backend engineering and distributed systems implementations.
