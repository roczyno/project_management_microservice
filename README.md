# Project Management Microservices

A comprehensive project management system built with a microservices architecture using Spring Boot and Spring Cloud.

## Architecture Overview

This project follows a microservices architecture pattern with the following components:

- **API Gateway**: Central entry point that routes requests to appropriate services
- **Config Server**: Centralized configuration management
- **Discovery Service**: Service registry for dynamic service discovery
- **Multiple Domain-Specific Microservices**: Each handling specific business functionality

![Architecture Diagram](https://miro.medium.com/v2/resize:fit:1400/1*QzP9v1ovZdToyThz2H0QAA.png)

## Microservices

| Service | Description | Main Endpoints |
|---------|-------------|---------------|
| **User Service** | Handles user management, authentication, and authorization | `/api/v1/user/**`, `/api/v1/auth/**` |
| **Project Service** | Manages projects, teams, and project-related operations | `/api/v1/project/**` |
| **Issue Service** | Tracks and manages project issues/tickets | `/api/v1/issue/**` |
| **Comment Service** | Handles comments on issues and projects | `/api/v1/comment/**` |
| **Chat Service** | Provides real-time communication between users | `/api/v1/chat/**`, `/api/v1/message/**` |
| **Invitation Service** | Manages invitations to projects and teams | `/api/v1/invite/**` |
| **Notification Service** | Sends notifications to users | Internal service |
| **Subscription Service** | Manages subscription plans and billing | Internal service |

## Technologies Used

- **Spring Boot**: For creating standalone microservices
- **Spring Cloud**: For cloud-native patterns
  - Netflix Eureka: Service discovery
  - Spring Cloud Config: Centralized configuration
  - Spring Cloud Gateway: API Gateway
  - OpenFeign: Service-to-service communication
  - Circuit Breaker: Resilience patterns
- **Databases**:
  - PostgreSQL: For relational data
  - MongoDB: For document-based data
- **Messaging**:
  - Apache Kafka: For asynchronous communication between services
- **Monitoring and Tracing**:
  - Zipkin: Distributed tracing
  - Spring Actuator: Health monitoring
- **DevOps**:
  - Docker: Containerization
  - Docker Compose: Multi-container deployment

## Prerequisites

- Java 17+
- Docker and Docker Compose
- Maven
- PostgreSQL (if running locally)
- MongoDB (if running locally)

## Getting Started

### Running with Docker Compose

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd project-management-microservices
   ```

2. Start the infrastructure services:
   ```bash
   docker-compose up -d
   ```

3. Build and run the microservices:
   ```bash
   mvn clean package
   java -jar services/config-server/target/config-server.jar
   java -jar services/discovery-service/target/discovery-service.jar
   # Start other services as needed
   ```

### Development Setup

1. Start the infrastructure services using Docker Compose:
   ```bash
   docker-compose up -d postgres mongoDB kafka zookeeper zipkin
   ```

2. Run each microservice individually in your IDE or using Maven:
   ```bash
   cd services/config-server
   mvn spring-boot:run
   
   # In a new terminal
   cd services/discovery-service
   mvn spring-boot:run
   
   # Continue for other services
   ```

## API Documentation

The API Gateway exposes the following main endpoints:

- **Authentication**: `/api/v1/auth/**`
  - POST `/api/v1/auth/register`: Register a new user
  - POST `/api/v1/auth/login`: Authenticate a user

- **User Management**: `/api/v1/user/**`
  - GET `/api/v1/user/{id}`: Get user details
  - PUT `/api/v1/user/{id}`: Update user details

- **Project Management**: `/api/v1/project/**`
  - POST `/api/v1/project`: Create a new project
  - GET `/api/v1/project/{id}`: Get project details
  - PUT `/api/v1/project/{id}`: Update project details
  - DELETE `/api/v1/project/{id}`: Delete a project

- **Issue Tracking**: `/api/v1/issue/**`
  - POST `/api/v1/issue`: Create a new issue
  - GET `/api/v1/issue/{id}`: Get issue details
  - PUT `/api/v1/issue/{id}`: Update issue details

- **Comments**: `/api/v1/comment/**`
  - POST `/api/v1/comment`: Add a comment
  - GET `/api/v1/comment/issue/{issueId}`: Get comments for an issue

- **Chat**: `/api/v1/chat/**`, `/api/v1/message/**`
  - Various endpoints for real-time communication

- **Invitations**: `/api/v1/invite/**`
  - POST `/api/v1/invite`: Send an invitation
  - PUT `/api/v1/invite/{id}/accept`: Accept an invitation
  - PUT `/api/v1/invite/{id}/reject`: Reject an invitation

## Monitoring

- **Zipkin**: http://localhost:9411 - For distributed tracing
- **Eureka Dashboard**: http://localhost:8761 - For service registry status
- **MongoDB Express**: http://localhost:8081 - For MongoDB administration
- **MailDev**: http://localhost:1080 - For email testing

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
