# ikeyit-common-data-spring

A Spring-based extension of ikeyit-common-data that provides implementation for domain event handling and transaction management in Spring applications.

## Features

### Domain Event Integration
- Seamless integration with Spring's event system
- Support for transactional event handling
- Persistent domain event storage and retry mechanism
- At-least-once event processing guarantee

### Transaction Management
- Transaction-aware event listeners
- Support for different transaction phases
- Automatic event persistence and cleanup

## Usage

### Configuration

1. Add the following dependency to your project:

```gradle
dependencies {
    implementation 'com.ikeyit:ikeyit-common-data-spring'
}
```

2. Enable domain event publishing in your Spring Boot application:

```java
@SpringBootApplication
@EnablePublishDomainEvent
@EnablePersistDomainEvent
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### Working with Domain Events

1. Define your domain event:
```java
public class OrderCreatedEvent extends BaseDomainEvent {
    private final OrderId orderId;
    
    public OrderCreatedEvent(OrderId orderId) {
        this.orderId = orderId;
    }
    
    public OrderId getOrderId() {
        return orderId;
    }
}
```

2. Implement event listener with transaction support:
```java
@Component
public class OrderEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @PersistDomainEvent
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Process the event
        // This code will run after the transaction commits
        // If processing fails, the event will be retried
    }
}
```

### Important Notes

1. Always use `@PersistDomainEvent` together with `@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)`
2. Event listeners should be idempotent as events may be processed multiple times
3. Events are automatically cleaned up after successful processing
4. Failed events will be retried based on the configured retry policy

## Features Details

### Persistent Event Handling
- Events are stored in the database as part of the transaction
- Automatic cleanup after successful processing
- Built-in retry mechanism for failed events
- Support for distributed transaction patterns like SAGA

## Best Practices

1. Keep event handlers idempotent
2. Use meaningful event names and include necessary context
3. Handle exceptions appropriately in event listeners
4. Monitor event processing and handle failed events
5. Configure appropriate retry policies for your use case