# ikeyit-common-data

A foundational Java library providing core data handling components and domain-driven design (DDD) building blocks for the ikeyit-aiarch project.

## Features

### Domain-Driven Design Support
- `AggregateRoot` and `BaseAggregateRoot`: Base classes for implementing DDD aggregate roots
- `Entity`: Base interface for domain entities
- `DomainEvent` and `BaseDomainEvent`: Support for domain events
- `CrudRepository`: Generic repository interface for data persistence
- Domain event handling with `PersistDomainEvent` and `PublishDomainEvent` annotations

### Working with Domain Events
Use @PublishDomainEvent to publish all domain events stored in an aggregate root when the annotated method is called.
When aggregate roots are saved or updated into database, you should use this annotation to publish domain events.
```java
@Repository
public class JdbcOrderRepository implements OrderRepository {
    @Override
    @PublishDomainEvent
    public void create(Order order) {
        // ...
    }
}
```
Always use @PersistDomainEvent together with @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT).
@TransactionalEventListener allows the event listener to be called in the different transaction phase. By setting TransactionPhase.AFTER_COMMIT, the listener is called only after the current transaction is committed successfully.
Annotated by @PersistDomainEvent, the domain event is saved into db as a part of the transaction, and removed after the listener is executed without any exception.
If the domain event is not removed after a duration, the listener will be re-executed.
This machinasm ensures that the domain event is processed successfully at least once, but it may be processed multiple times.
So the listener should be idempotent. It's useful to implement the SAGA pattern of distributed transaction.
```java
@Component
public class StoreListener {

    @TransactionalEventListener(id = "grantOwnerOnStoreCreatedEvent")
    @PersistDomainEvent
    public void grantOwnerOnStoreCreatedEvent(StoreCreatedEvent event) {
        // ...
    }
}
```

### Data Structures and Utilities
- `Page` and `Pageable`: Pagination support
- `PageParam` and `SortParam`: Request parameters for pagination and sorting
- `Pair`: Generic pair data structure
- `JsonUtils`: JSON serialization and deserialization utilities
- `IdUtils`: ID generation and handling utilities
- `PrivacyUtils`: Data privacy protection utilities

### Field Management
- `IncludedFields`: Field inclusion/exclusion management
- `JsonField`: JSON field handling
- `Embedded`: Support for embedded objects

### Enum Support
- `EnumWithInt`: Interface for enums with integer values

## Usage

### Implementing Domain Entities
```java
public class Order extends BaseAggregateRoot {
    private OrderId id;
    private CustomerId customerId;
    private List<OrderItem> items;
    
    // Domain logic and behavior
}
```

### Using Pagination
```java
// Create page parameters
PageParam pageParam = new PageParam(1, 20);
SortParam sortParam = new SortParam("createdTime", "DESC");

// Get paginated results
Page<Order> orders = orderRepository.findAll(pageParam, sortParam);
```
