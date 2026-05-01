# 🚀 Quick Commerce System (Microservices)

A distributed backend system simulating a quick commerce platform (like Blinkit / Swiggy Instamart), built using Spring Boot microservices architecture with focus on scalability, fault tolerance, and system design principles.

---

## 🧠 Overview

This project demonstrates how real-world backend systems are designed using:

- Microservices architecture  
- Service-to-service communication  
- Failure handling & resilience  
- Distributed system design concepts  

---

## 🧩 Architecture

Client → Order Service → Inventory Service

- Order Service communicates with Inventory Service using REST APIs  
- Implements retry, timeout, and failure handling  
- Designed to evolve into event-driven architecture (Kafka)

---

## ⚙️ Tech Stack

- Java 17+
- Spring Boot
- REST APIs
- Maven
- Docker (for future integrations)
- PostgreSQL (planned)
- Resilience4j (upcoming)
- Kafka (upcoming)

---

## 📦 Services

### 🧾 Order Service
- Handles order creation  
- Calls Inventory Service to validate stock  
- Implements:
  - Retry mechanism  
  - Timeout handling  
  - Graceful failure handling  

---

### 📦 Inventory Service
- Manages product stock  
- Provides APIs to:
  - Add inventory  
  - Check availability  

---

## 🔥 Features Implemented

- ✅ Microservices-based architecture  
- ✅ REST communication between services  
- ✅ Retry mechanism (manual implementation)  
- ✅ Timeout handling  
- ✅ Fault tolerance using try-catch  
- ✅ Basic logging for observability  

---

## 💣 System Design Concepts Covered

- Synchronous communication (REST)  
- Retry strategy  
- Timeout management  
- Fault tolerance  
- Graceful degradation  
- Handling transient vs permanent failures  

---

## 🚀 Upcoming Enhancements

- 🔁 Circuit Breaker (Resilience4j)  
- 📩 Event-driven architecture using Kafka  
- ⚡ Redis caching  
- 🌐 API Gateway  
- 🔍 Elasticsearch for search optimization  
- 🐳 Docker Compose for full system setup  

---

## ▶️ How to Run

### 1. Start Inventory Service
cd inventory-service ./mvnw spring-boot:run

### 2. Start Order Service
cd order-service ./mvnw spring-boot:run

---

## 🧪 API Testing

### Add Inventory
POST /inventory/add?productId=1&quantity=10

### Check Inventory
GET /inventory/check?productId=1&quantity=5

### Create Order
POST /orders {   "productId": 1,   "quantity": 2 }

---

## 🧠 Learning Outcome

This project focuses on building production-ready backend systems with emphasis on:

- Distributed system design  
- Microservices communication patterns  
- Failure handling strategies  
- Scalability and resilience  

---

## 👨‍💻 Author

Swapnil  
Backend Engineer | Java | Spring Boot | Microservices  

---

## ⭐ If you like this project

Give it a ⭐ on GitHub and follow for more system design implementatio
