# Order Management System

A modern, enterprise-grade order management system built with Spring Boot 4 and Java 21, providing secure REST APIs for managing orders, products, and customer transactions with advanced discount strategies and payment processing.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Database Schema](#database-schema)
- [Project Structure](#project-structure)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Security](#security)
- [Troubleshooting](#troubleshooting)

---

## Overview

The Order Management System is a comprehensive backend application designed to handle complex order processing with support for:
- **User Authentication & Authorization**: JWT-based authentication with role-based access control
- **Product Management**: Admin-controlled product catalog with inventory tracking
- **Order Processing**: Customer order creation with automatic discount calculation
- **Dynamic Discount Strategies**: VIP and bulk order discounts based on customer type
- **Payment Integration**: Streamlined payment processing for orders
- **Database Versioning**: Liquibase-based schema management with PostgreSQL

---

## Features

### 🔐 Security & Authentication
- JWT token-based authentication
- Spring Security 6 integration
- Role-based access control (RBAC)
- User roles: ADMIN, CUSTOMER
- Bearer token authorization
- Password encryption with BCrypt

### 📦 Product Management
- Create and manage products
- Track product inventory/stock
- View low-stock products (admin feature)
- Product search and details retrieval
- Stock quantity validation

### 🛒 Order Management
- Create customer orders with validation
- Add multiple items per order
- Automatic order total calculation
- Order history and retrieval
- Customer type-based discounting
- Order status tracking

### 💰 Payment Processing
- Secure payment handling
- Multiple payment methods support:
  - Credit Card
  - Debit Card
  - PayPal
  - Bank Transfer
  - Cash
- Order finalization with payment

### 🎁 Discount Strategies
- **VIP Discount**: 20% discount for VIP customers
- **Bulk Discount**: 10% discount for orders with 10+ items
- Automatic strategy selection based on customer type
- Flexible order pricing calculation

### 📊 Database Management
- PostgreSQL integration
- Liquibase version control for schemas
- Automatic schema initialization
- Data migration support

---

## Technology Stack

| Layer | Technology |
|-------|-----------|
| **Framework** | Spring Boot 4.0.5 |
| **Language** | Java 21 |
| **Database** | PostgreSQL |
| **JPA/ORM** | Spring Data JPA, Jakarta Persistence |
| **Security** | Spring Security 6, JWT (JJWT 0.11.5) |
| **Build Tool** | Maven 3.x |
| **Schema Migration** | Liquibase |
| **Code Generation** | Lombok |
| **Validation** | Jakarta Bean Validation |
| **Web** | Spring WebMVC, Tomcat |

---

## Prerequisites

- **Java 21** or higher
- **Maven 3.8+**
- **PostgreSQL 12+** running locally or remotely
- **Git** (for version control)
- A compatible IDE (IntelliJ IDEA, VS Code, Eclipse)

### System Requirements
- Minimum RAM: 2GB
- Disk Space: 500MB
- Network connectivity to PostgreSQL server

---

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd order-system
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Configure PostgreSQL Database

Create a new PostgreSQL database:
```sql
CREATE DATABASE order_system;
CREATE USER order_user WITH ENCRYPTED PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE order_system TO order_user;
```

### 4. Configure Application Properties

Edit `src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: order-system
  datasource:
    url: jdbc:postgresql://localhost:5432/order_system
    username: order_user
    password: your_secure_password
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate  # Use 'validate' after Liquibase init
    show-sql: false
  liquibase:
    change-log: classpath:db/changelog/db.changelog-master.xml
    enabled: true
```

---

## Configuration

### Environment-Specific Profiles

The application supports three profiles:

- **dev** (`application-dev.yml`): Development environment with detailed logging
- **prod** (`application-prod.yml`): Production environment with optimized settings
- **test** (`application-test.yml`): Test environment with in-memory/test database

Set active profile:
```bash
# Run with dev profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Or set in application.yml
spring:
  profiles:
    active: dev
```

### Security Configuration

JWT configuration is managed in `SecurityConfig`. Key settings:
- Secret key for token signing (configure in application properties)
- Token expiration time (default: 24 hours)
- Excluded endpoints: `/api/auth/login`, `/api/auth/register`

---

## API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Register new user | None |
| POST | `/api/auth/login` | User login, returns JWT | None |

**Login Request:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Login Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 86400
}
```

---

### Product Endpoints

| Method | Endpoint | Description | Auth | Role |
|--------|----------|-------------|------|------|
| POST | `/api/products/create` | Create new product | Required | ADMIN |
| GET | `/api/products/{id}` | Get product by ID | Required | ADMIN, CUSTOMER |
| GET | `/api/products/low-stock?value=50` | Get products with low stock | Required | ADMIN |

**Create Product Request:**
```json
{
  "name": "Laptop",
  "price": 999.99,
  "stockQuantity": 50
}
```

**Product Response:**
```json
{
  "id": 1,
  "name": "Laptop",
  "price": 999.99,
  "stockQuantity": 50
}
```

---

### Order Endpoints

| Method | Endpoint | Description | Auth | Role |
|--------|----------|-------------|------|------|
| POST | `/api/orders/create` | Create new order | Required | CUSTOMER |
| GET | `/api/orders/{id}` | Get order by ID | Required | ADMIN, CUSTOMER |

**Create Order Request:**
```json
{
  "customerId": 1,
  "customerName": "Ahmed Hassan",
  "customerType": "VIP",
  "paymentMethod": "CREDIT_CARD",
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "unitPrice": 999.99
    }
  ]
}
```

**Order Response:**
```json
{
  "id": 1,
  "customerName": "Ahmed Hassan",
  "customerType": "VIP",
  "paymentMethod": "CREDIT_CARD",
  "totalPrice": 1999.98,
  "discountAmount": 399.996,
  "items": [
    {
      "id": 1,
      "productId": 1,
      "quantity": 2,
      "unitPrice": 999.99
    }
  ]
}
```

---

## Authentication

### Bearer Token Usage

Include JWT token in every request header:
```bash
curl -H "Authorization: Bearer <your_jwt_token>" \
     https://api.example.com/api/orders/1
```

### Token Expiration

Tokens expire after 24 hours by default. Request a new token by logging in again.

---

## Database Schema

The application uses Liquibase to manage database migrations. Initial schema includes:

### Products Table
```sql
CREATE TABLE products (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  price DOUBLE PRECISION NOT NULL,
  stock INTEGER NOT NULL
);
```

### Orders Table
```sql
CREATE TABLE orders (
  id SERIAL PRIMARY KEY,
  customer_name VARCHAR(255),
  customer_type VARCHAR(50),
  payment_method VARCHAR(50),
  total_price DOUBLE PRECISION,
  discount_amount DOUBLE PRECISION
);
```

### Order Items Table
```sql
CREATE TABLE order_items (
  id SERIAL PRIMARY KEY,
  order_id INTEGER REFERENCES orders(id),
  product_id INTEGER REFERENCES products(id),
  quantity INTEGER,
  unit_price DOUBLE PRECISION
);
```

---

## Project Structure

```
order-system/
├── src/
│   ├── main/
│   │   ├── java/com/pioneers/order_system/
│   │   │   ├── auth/                    # Authentication controllers & services
│   │   │   ├── controllers/             # REST endpoints
│   │   │   ├── dtos/                    # Data Transfer Objects
│   │   │   ├── entities/                # JPA entities
│   │   │   ├── enums/                   # Enumerations (CustomerType, PaymentMethod)
│   │   │   ├── errors/                  # Exception handling
│   │   │   ├── mappers/                 # Entity-DTO mappers
│   │   │   ├── payment/                 # Payment processing
│   │   │   ├── repositories/            # Data access layer
│   │   │   ├── security/                # Security configuration
│   │   │   ├── services/                # Business logic
│   │   │   │   └── discountstrategies/  # Discount strategy implementations
│   │   │   └── user/                    # User management
│   │   └── resources/
│   │       ├── application.yml          # Main configuration
│   │       ├── application-dev.yml      # Dev profile
│   │       ├── application-prod.yml     # Prod profile
│   │       ├── application-test.yml     # Test profile
│   │       └── db/changelog/            # Liquibase migrations
│   └── test/
│       └── java/com/pioneers/order_system/  # Unit & integration tests
├── pom.xml                              # Maven dependencies
└── README.md                            # This file
```

---

## Running the Application

### Option 1: Using Maven
```bash
# Clean build and run
mvn clean spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Build JAR and run
mvn clean package
java -jar target/order-system-0.0.1-SNAPSHOT.jar
```

### Option 2: Using IDE
1. Open the project in your IDE
2. Run `OrderSystemApplication.java` as a Java application
3. Application starts on `http://localhost:8080`

### Option 3: Docker (if configured)
```bash
docker build -t order-system:latest .
docker run -p 8080:8080 \
           -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/order_system \
           order-system:latest
```

---

**Last Updated:** June 2026

