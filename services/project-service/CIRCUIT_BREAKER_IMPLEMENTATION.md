# Circuit Breaker Implementation in Project Management Microservices

## Overview

This document describes the implementation of the Circuit Breaker pattern in the Project Management Microservices application. The Circuit Breaker pattern is used to prevent cascading failures in distributed systems by detecting failures and encapsulating the logic of preventing a failure from constantly recurring.

## Implementation in Project Service

The Project Service has been configured with circuit breakers for all external service calls using Resilience4j. The following external services are protected:

1. **User Service** - For user-related operations
2. **Chat Service** - For chat-related operations
3. **Subscription Service** - For subscription-related operations

### Configuration

The circuit breaker configuration is defined in `application.yml`:

```yaml
resilience4j:
  circuitbreaker:
    instances:
      userBreaker:
        register-health-indicator: true
        sliding-window-size: 10
        minimum-number-of-calls: 5
        permitted-number-of-calls-in-half-open-state: 3
        wait-duration-in-open-state: 10s
        failure-rate-threshold: 50
        automatic-transition-from-open-to-half-open-enabled: true
        sliding-window-type: count_based

      chatBreaker:
        register-health-indicator: true
        sliding-window-size: 10
        minimum-number-of-calls: 5
        permitted-number-of-calls-in-half-open-state: 3
        wait-duration-in-open-state: 10s
        failure-rate-threshold: 50
        automatic-transition-from-open-to-half-open-enabled: true
        sliding-window-type: count_based

      subscriptionBreaker:
        register-health-indicator: true
        sliding-window-size: 10
        minimum-number-of-calls: 5
        permitted-number-of-calls-in-half-open-state: 3
        wait-duration-in-open-state: 10s
        failure-rate-threshold: 50
        automatic-transition-from-open-to-half-open-enabled: true
        sliding-window-type: count_based

  retry:
    instances:
      chatBreaker:
        max-attempts: 5
        wait-duration: 10s
      userBreaker:
        max-attempts: 5
        wait-duration: 10s
      subscriptionBreaker:
        max-attempts: 5
        wait-duration: 10s

  ratelimiter:
    instances:
      chatBreaker:
        timeout-duration: 0
        limit-refresh-period: 4
        limit-for-period: 2
      userBreaker:
        timeout-duration: 0
        limit-refresh-period: 4
        limit-for-period: 2
      subscriptionBreaker:
        timeout-duration: 0
        limit-refresh-period: 4
        limit-for-period: 2
```

### Implementation Details

The circuit breaker pattern is implemented using the following annotations:

1. `@CircuitBreaker` - Applies the circuit breaker pattern to the method
2. `@Retry` - Retries the method call if it fails
3. `@RateLimiter` - Limits the rate of method calls

Each annotation is applied to methods that make external service calls, with a corresponding fallback method that is called when the circuit breaker is triggered.

Example:

```java
@CircuitBreaker(name = "chatBreaker", fallbackMethod = "createProjectFallback")
@Retry(name = "chatBreaker", fallbackMethod = "createProjectFallback")
@RateLimiter(name = "chatBreaker", fallbackMethod = "createProjectFallback")
public ProjectResponse createProject(ProjectRequest req, String jwt) {
    // Method implementation
}

public ProjectResponse createProjectFallback(ProjectRequest req, String jwt, Exception e) {
    log.error("Circuit breaker triggered in createProject: {}", e.getMessage(), e);
    // Fallback implementation
}
```

### Protected Methods

The following methods in the Project Service are protected with circuit breakers:

1. `createProject` - Protected with chatBreaker
2. `validateUserProjectLimit` - Protected with subscriptionBreaker
3. `getProjectByTeam` - Protected with userBreaker
4. `addUserToProject` - Protected with chatBreaker
5. `removeUserFromProject` - Protected with chatBreaker
6. `findProjectTeamByProjectId` - Protected with userBreaker
7. `validateOwnershipAndGetProject` - Protected with userBreaker

## Implementing Circuit Breakers in Other Services

To implement circuit breakers in other services, follow these steps:

1. Add the necessary dependencies to the service's `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

2. Configure the circuit breaker in the service's `application.yml`:

```yaml
resilience4j:
  circuitbreaker:
    instances:
      serviceBreaker:
        register-health-indicator: true
        sliding-window-size: 10
        minimum-number-of-calls: 5
        permitted-number-of-calls-in-half-open-state: 3
        wait-duration-in-open-state: 10s
        failure-rate-threshold: 50
        automatic-transition-from-open-to-half-open-enabled: true
        sliding-window-type: count_based
  retry:
    instances:
      serviceBreaker:
        max-attempts: 5
        wait-duration: 10s
  ratelimiter:
    instances:
      serviceBreaker:
        timeout-duration: 0
        limit-refresh-period: 4
        limit-for-period: 2
```

3. Add the necessary imports to the service implementation:

```java
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
```

4. Apply the circuit breaker annotations to methods that make external service calls:

```java
@CircuitBreaker(name = "serviceBreaker", fallbackMethod = "methodFallback")
@Retry(name = "serviceBreaker", fallbackMethod = "methodFallback")
@RateLimiter(name = "serviceBreaker", fallbackMethod = "methodFallback")
public ReturnType methodName(String param1, Integer param2) {
    // Method implementation
}

public ReturnType methodFallback(String param1, Integer param2, Exception e) {
    log.error("Circuit breaker triggered in methodName: {}", e.getMessage(), e);
    // Fallback implementation
}
```

## Services That Need Circuit Breaker Implementation

The following services have Feign clients and would benefit from circuit breaker implementation:

1. **Chat Service** - Calls to PROJECT-SERVICE and USER-SERVICE
2. **Comment Service** - Calls to USER-SERVICE
3. **Invitation Service** - Calls to PROJECT-SERVICE and USER-SERVICE
4. **Issue Service** - Calls to PROJECT-SERVICE and USER-SERVICE
5. **Subscription Service** - Calls to USER-SERVICE
6. **User Service** - Calls to SUBSCRIPTION-SERVICE

## Testing

To test the circuit breaker implementation, you can:

1. Start the application with one of the dependent services down
2. Make a request that would normally call the down service
3. Verify that the fallback method is called and the application continues to function

## Monitoring

The circuit breaker status can be monitored using Spring Boot Actuator. Add the following to your `application.yml`:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,circuitbreakers
  health:
    circuitbreakers:
      enabled: true
```

Then you can access the circuit breaker status at `/actuator/circuitbreakers`.
