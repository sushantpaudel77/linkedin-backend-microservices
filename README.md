## 🛠️ TODO: Externalized Configuration with Spring Cloud Config

📌 **Note:** Currently, all microservices use **hardcoded or local `application.yml` files** for configuration.  
To improve maintainability and enable centralized configuration management:

- [ ] Integrate a **Spring Cloud Config Server**
- [ ] Move all service-specific properties (ports, DB credentials, Kafka topics, etc.) to a shared config repo
- [ ] Connect each microservice to fetch configuration from the Config Server at startup
- [ ] (Optional) Add support for dynamic refresh using Spring Actuator + Bus

🔗 Official Docs: [Spring Cloud Config](https://spring.io/projects/spring-cloud-config)

# 🐳 LinkedIn Backend Microservices — Full Docker Compose Stack

This is a complete **monolithic `docker-compose.yml` setup** for my LinkedIn backend clone.

It includes:

- Kafka & Kafka UI
- PostgreSQL Databases
- Neo4j (Graph DB for Connections)
- Spring Boot Microservices
- Eureka Discovery Server
- API Gateway

> ✅ **Note**: I’ve modularized this setup into **three smaller Compose files** for better management in most workflows.

This file serves as an **all-in-one alternative** — ideal for **quick local testing** or when a unified stack is easier to manage.

---

## 🔀 See the Modular Files

For advanced or staged deployments, use the split Compose files:

- [`docker-compose.base.yml`](./docker-compose.base.yml)
- [`docker-compose.kafka-zipkin.yml`](./docker-compose.kafka-zipkin.yml)
- [`docker-compose.posts-user.yml`](./docker-compose.posts-user.yml)

---

## 🚀 How to Run

### 🔧 Split Compose Files (Recommended)

```bash
docker-compose -f docker-compose.kafka.yml \
               -f docker-compose.db.yml \
               -f docker-compose.services.yml up

🧱 All-in-One Compose
bash
Copy
Edit
docker-compose up --build

☸️ Kubernetes Deployment
bash
Copy
Edit
kubectl apply -f k8s/

🔐 Secrets & Environment Variables
env
Copy
Edit
JWT_SECRET_KEY=my_super_secret_key

🧠 Monitoring & Debugging
Tool	URL	Notes
Eureka Dashboard	http://localhost:8761	Service registration monitor
Kafka UI	http://localhost:8090	View topics, messages
Neo4j Browser	http://localhost:7474	Graph DB for connections
Zipkin (Optional)	Add tracing support later	Distributed tracing


📦 Microservices Overview
Service	Port	Description
🧭 API Gateway	8080	Entry point for all requests
🧬 Discovery Server	8761	Eureka registry for service discovery
👤 User Service	9011	Handles user authentication & profiles
📝 Posts Service	9010	Manages posts and user feed
🔔 Notifications	9012	Sends and stores notifications
🔗 Connections	9013	Manages user connections (Neo4j)

🧪 Testing & Validation
Use the following tools to test and validate the setup:
🧪 Postman / Thunder Client for REST API testing
🛰️ Kafka UI to inspect message topics and brokers
🌐 Eureka Dashboard to verify microservice registration
📜 Logs via Docker or K8s to debug errors or issues

👤 Author
Sushant Paudel
🔗 GitHub
🐳 DockerHub

💬 Feel free to fork, contribute, or raise issues! This project is built for learning and scale experimentation.

yaml
Copy
Edit
---

✅ This format is professional, readable, developer-friendly, and ideal for both GitHub and portfolio presentation.  
Would you like me to generate a `.md` file or zip up the folder structure too?
