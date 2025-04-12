# ikeyit-account

## Overview

The `ikeyit-account` module is a core component responsible for user account management, authentication, authorization and SSO. 

## Key Features

### User Management

- User registration with email/phone verification
- Profile management (avatar, display name, gender, location, locale preferences)
- Security management (password, phone, email)

### Authentication & Authorization

- Multiple authentication methods (password, verification code, social login)
- Remember-me functionality
- IDP (Identity Provider) supporting OIDC (OpenId Connect) protocol. Enable SSO (Single Sign-on) service for other systems.

## Architecture

This project follows a clean architecture based on DDD principles, organized into the following layers:

### Domain Layer

The core business logic and entities reside in the domain layer:

- **Entities**: `User`, `UserConnection`, etc.
- **Repositories**: `UserRepository`, `UserConnectionRepository`
- **Domain Events**: Various user-related events like `UserCreatedEvent`, `UserProfileUpdatedEvent`, etc.

### Application Layer

The application layer orchestrates the domain objects to perform use cases:

- **Services**: `UserService` - handles user registration, profile updates, password management, etc.
- **DTOs**: Data Transfer Objects for input/output across boundaries
- **Validators**: `PasswordValidator`, `ContactInfoValidator` for business rule validation

### Infrastructure Layer

Implements technical capabilities and adapters to external systems:

- **Repositories**: Implementations of domain repositories
- **Security**: Authentication and authorization mechanisms
- **External Services**: Email/SMS integration, etc.

### Interfaces Layer

Multiple deployment options for different communication protocols:

- **REST API** (`ikeyit-account-interfaces-api`): RESTful endpoints for account management
- **gRPC** (`ikeyit-account-interfaces-grpc`): High-performance RPC interfaces
- **Message Consumers** (`ikeyit-account-interfaces-consumer`): Event-driven interfaces
- **Scheduled Jobs** (`ikeyit-account-interfaces-job`): Background processing
- **All-in-One** (`ikeyit-account-interfaces-allinone`): Monolithic deployment option

## Setup and Configuration

### Prerequisites

- Java 21+
- Gradle 8.13+
- PostgreSQL 14+
- Redis 6+
- S3-compatible object storage (AWS S3, MinIO, etc.)

### Configuration
- Initialize the database

Run the sql
[account-schema.sql](ikeyit-account-infrastructure/src/main/resources/db/account-schema.sql)

- Configure properties

Refer to
[application.yml](ikeyit-account-interfaces-allinone/src/main/resources/application.yml)
[application-dev.yml](ikeyit-account-interfaces-allinone/src/main/resources/application-dev.yml)

Key points: database, redis, blobstore

### Building the Project

```bash
# Build all modules
./gradlew :ikeyit-account:build

# Build specific module
./gradlew :ikeyit-account:ikeyit-account-interfaces-api:build
```

### Running the Application

```bash
# Run the all-in-one application
./gradlew :ikeyit-account:ikeyit-account-interfaces-allinone:bootRun
```

## Integration
This project includes only the backend. You need integrate it with the front ikeyit-account-client to make all things work.
> **NOTE**
> The backend and the frontend should be deployed in the same domain. Use reverse proxy (nginx, kubernetes ingress/gateway) for your production environment. 
> In the dev environment, the ikeyit-account-client project has been configured to use proxy for this  (see vite.config.js)
### Client SDK

The `ikeyit-account-sdk` module provides client libraries for integrating with the account service from other internal services.

### API Documentation

REST API documentation is available in OpenAPI format at
[ikeyit-account-openapi.yml](ikeyit-account-interfaces-api/ikeyit-account-openapi.yml)