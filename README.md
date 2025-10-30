# 🧩 Order Processing System – PeerIslands Take-Home Assignment

This project is a **containerized Order Processing System** built using **Spring Boot**, **Kafka**, and **H2 Database**, all managed through **Docker Compose**.

It demonstrates asynchronous **order status management** using Kafka — hence, **no cron jobs** are used.  
Status updates (like moving from `PENDING` → `PROCESSING`) happen automatically via **Kafka events**.

---

## 🚀 Features

- 🛒 Create and manage customer orders with multiple items  
- 🔄 Asynchronous order status updates using Kafka events  
- 🔍 Retrieve order details by ID or status filter  
- ❌ Cancel orders (only if still `PENDING`)  
- 🧠 Fully containerized setup (Spring Boot + Kafka + Zookeeper + H2)  
- 🧪 In-memory database with H2 console for debugging  

---

## 🧱 Tech Stack

| Layer | Technology |
|--------|-------------|
| Backend | Spring Boot 3.x |
| Messaging | Apache Kafka |
| Database | H2 (in-memory) |
| Containerization | Docker & Docker Compose |
| Build Tool | Maven |
| Testing | JUnit 5, Mockito |

---

## ⚙️ Project Structure

```text
order-processing-system/
├── docker-compose.yml          # Defines services for App, Kafka, Zookeeper
├── Dockerfile                  # Builds the Spring Boot app image
├── pom.xml                     # Maven dependencies & build configuration
├── README.md
└── src/
    ├── main/
    │   ├── java/com/peerislands/assignment/order_processing_system/
    │   │   ├── controller/      # REST APIs
    │   │   ├── service/         # Business logic
    │   │   ├── event/           # Kafka Producer/Consumer logic
    │   │   ├── entity/          # JPA Entities (Order, OrderItem)
    │   │   └── repository/      # Spring Data JPA Repositories
    │   └── resources/
    │       ├── application.yml  # App, Kafka & DB configuration
🧩 Prerequisites

Ensure you have these installed before proceeding:

Java 17+

Maven 3.9+

Docker Desktop or Docker Engine 24+

🐳 Run the Application (All-in-One via Docker)

Everything — the app, Kafka, and Zookeeper — runs in containers.

Step 1️⃣: Build the project
mvn clean package -DskipTests

Step 2️⃣: Start all services
docker-compose up --build


This spins up:

Zookeeper on port 2181

Kafka Broker on port 9092

Spring Boot App on port 8080

Step 3️⃣: Verify all containers
docker ps


Expected:

zookeeper
kafka
order-processing-system-app

Step 4️⃣: Access the application
Service	URL
REST API Base	http://localhost:8080/api/orders
H2 Console	http://localhost:8080/h2-console
Kafka Broker	localhost:9092

H2 Console Credentials:

JDBC URL: jdbc:h2:mem:ordersdb
Username: sa
Password: (leave blank)

🌐 API Endpoints
Method	Endpoint	Description
POST	/api/orders	Create a new order
GET	/api/orders/{id}	Fetch order details by ID
GET	/api/orders?status=PENDING	List orders filtered by status
PUT	/api/orders/{id}/cancel	Cancel an order (only if PENDING)
🧠 Kafka Topics
Topic	Purpose
order-events	Publishes all order creation & status change events
🧪 Example Postman Requests
▶ Create Order

POST → http://localhost:8080/api/orders

Body (JSON):

{
  "customerName": "Vijay Patil",
  "items": [
    { "productName": "Laptop", "quantity": 1, "price": 75000 },
    { "productName": "Mouse", "quantity": 2, "price": 1200 }
  ]
}


Expected Response:

{
  "id": 1,
  "customerName": "Vijay Patil",
  "status": "PENDING",
  "items": [
    { "productName": "Laptop", "quantity": 1, "price": 75000 },
    { "productName": "Mouse", "quantity": 2, "price": 1200 }
  ]
}

🔍 Get All Orders

GET → http://localhost:8080/api/orders

OR Filter by status:

GET http://localhost:8080/api/orders?status=PROCESSING

❌ Cancel an Order

PUT → http://localhost:8080/api/orders/1/cancel

Response:

{
  "message": "Order 1 cancelled successfully"
}

⚙️ How It Works (No Cron Jobs 🚫)

When an order is created, it is saved with status PENDING.

A Kafka event is published to order-events.

A Kafka listener automatically updates PENDING → PROCESSING asynchronously.

This removes the need for any scheduled cron job.

🧪 Run Tests
mvn test


Includes:

Valid/Invalid transitions

Concurrent cancellation tests

Kafka event publishing validation

🧼 Stop Containers

To shut down all running containers:

docker-compose down
    │       └── data.sql         # Optional sample data
    └── test/                    # Unit & integration tests
