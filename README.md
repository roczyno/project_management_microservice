# Project Management Microservices

A comprehensive project management system built with a microservices architecture using Spring Boot and Spring Cloud.

## Architecture Overview

This project follows a microservices architecture pattern with the following components:

- **API Gateway**: Central entry point that routes requests to appropriate services
- **Config Server**: Centralized configuration management
- **Discovery Service**: Service registry for dynamic service discovery
- **Multiple Domain-Specific Microservices**: Each handling specific business functionality

![Architecture Diagram](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=taskManager.drawio&dark=auto#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D16X65zEjo0brillBOS_Zh9HiAoulYogUH%26export%3Ddownload)

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
  - Redis: For caching
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
- Redis (if running locally)

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
   docker-compose up -d postgres mongoDB redis kafka zookeeper zipkin
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

## Caching

The project uses Redis for caching to improve performance and reduce database load. The caching implementation includes:

### Configuration

- Redis is configured in the config server for centralized management
- Cache TTL (Time-To-Live): 60000ms (60 seconds)
- Null values are not cached

### Cached Entities

- **Project Service**:
  - `projects`: Individual project details
  - `userProjects`: Projects associated with a user
  - `projectTeams`: Team members of a project
  - `projectSearch`: Project search results

- **User Service**:
  - `users`: Individual user details
  - `userProfiles`: User profile information
  - `userLists`: Lists of users

### Cache Eviction

Cache entries are automatically evicted when the underlying data changes:
- When a project is updated or deleted
- When users are added to or removed from projects
- When user project counts are modified

### Implementation

The caching is implemented using Spring Cache annotations:
- `@Cacheable`: For caching method results
- `@CacheEvict`: For removing cache entries when data changes
- `@CachePut`: For updating cache entries

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Submit a pull request

## Circuit Breaker Pattern

The project implements the Circuit Breaker pattern using Resilience4j to prevent cascading failures in distributed systems. This pattern helps maintain system stability when external services fail.

### Implementation

The Project Service has been configured with circuit breakers for all external service calls:

- **User Service** - For user-related operations
- **Chat Service** - For chat-related operations
- **Subscription Service** - For subscription-related operations

### How It Works

1. **Circuit Breaker**: Detects failures and prevents repeated calls to failing services
2. **Retry**: Attempts to retry failed calls before triggering the circuit breaker
3. **Rate Limiter**: Limits the rate of calls to external services

### Fallback Mechanisms

Each protected method has a corresponding fallback method that is called when the circuit breaker is triggered. For example:

- When chat service is down, projects can still be created without chat integration
- When user service is down, non-critical operations return empty results
- When subscription service is down, conservative limits are applied

### Configuration

Circuit breakers are configured in each service's `application.yml` file with parameters like:
- Failure threshold
- Sliding window size
- Wait duration in open state
- Retry attempts

For detailed implementation information, see the `CIRCUIT_BREAKER_IMPLEMENTATION.md` file in the project-service directory.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
