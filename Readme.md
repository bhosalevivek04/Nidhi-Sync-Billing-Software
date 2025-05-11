# NidhiSync Billing Software

**NidhiSync** is a Spring Boot-based billing & invoicing backend that supports:
- User management (Admin, Clerk, User roles)
- Category, Product, Customer, Invoice CRUD
- JWT-based security
- Barcode generation & lookup
- PDF invoice generation & download/email
- Analytics: monthly sales, revenue chart, top products

---

## Table of Contents

1. [Tech Stack](#tech-stack)  
2. [Getting Started](#getting-started)  
3. [Configuration](#configuration)  
4. [Authentication](#authentication)  
5. [Endpoints](#endpoints)  
   - [Auth](#auth)  
   - [Users (Admin only)](#users-admin-only)  
   - [Profile](#profile)  
   - [Categories](#categories)  
   - [Products](#products)  
   - [Customers](#customers)  
   - [Invoices](#invoices)  
   - [Barcode](#barcode)  
   - [Analytics (Admin only)](#analytics-admin-only)  
6. [Future Work](#future-work)

---

## Tech Stack

- Java 21, Spring Boot 3.4  
- Spring Security + JWT (io.jsonwebtoken)  
- Spring Data JPA (MySQL)  
- Springdoc OpenAPI / Swagger UI  
- iTextPDF for PDF generation  
- JavaMailSender for email  
- ZXing for barcode images  
- Lombok, Validation (Jakarta Bean Validation)

---

## Getting Started

1. **Clone & Build**  

```bash
   git clone https://github.com/bhosalevivek04/Nidhi-Sync-Billing-Software.git

   mvn clean install
````

2. **Configure**
   Edit `src/main/resources/application.properties` (or override via env):

   ```properties
   spring.datasource.url=jdbc:mysql://<host>:3306/billing_db
   spring.datasource.username=<db-user>
   spring.datasource.password=<db-pass>
   spring.jpa.hibernate.ddl-auto=update

   spring.mail.host=smtp.gmail.com
   spring.mail.username=<your-email>
   spring.mail.password=<your-mail-password>
   spring.mail.properties.mail.smtp.starttls.enable=true
   ```

3. **Run**

   ```bash
   mvn spring-boot:run
   ```

   Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## Authentication

All protected endpoints require an `Authorization: Bearer <token>` header.

1. **Register**

   ```http
   POST /api/auth/register
   Content-Type: application/json

   {
     "username":"alice",
     "email":"alice@example.com",
     "password":"secret123",
     "mobileNumber":"+911234567890"
   }
   ```

   → returns new user profile DTO.

2. **Login**

   ```http
   POST /api/auth/login
   Content-Type: application/json

   {
     "username":"alice",
     "password":"secret123"
   }
   ```

   → `{ "token": "eyJhbGciOiJIUzI1Ni..." }`

---

## Endpoints

### Auth

| Method | Path                 | Roles | Body            | Response            |
| ------ | -------------------- | ----- | --------------- | ------------------- |
| POST   | `/api/auth/register` | ―     | RegisterRequest | `UserResponseDto`   |
| POST   | `/api/auth/login`    | ―     | AuthRequestDto  | `{ token: String }` |

### Users (Admin only)

| Method | Path                    | Roles | Body               | Response            |
| ------ | ----------------------- | ----- | ------------------ | ------------------- |
| POST   | `/api/users`            | ADMIN | `UserRequestDto`   | `UserResponseDto`   |
| PUT    | `/api/users/{id}/roles` | ADMIN | `{ roles: [...] }` | `UserResponseDto`   |
| GET    | `/api/users`            | ADMIN | ―                  | `UserResponseDto[]` |
| GET    | `/api/users/{id}`       | ADMIN | ―                  | `UserResponseDto`   |
| DELETE | `/api/users/{id}`       | ADMIN | ―                  | 204 No Content      |

### Profile

| Method | Path           | Roles             | Body | Response          |
| ------ | -------------- | ----------------- | ---- | ----------------- |
| GET    | `/api/profile` | any authenticated | ―    | `UserResponseDto` |

### Categories

| Method | Path                   | Roles            | Body                 | Response                |
| ------ | ---------------------- | ---------------- | -------------------- | ----------------------- |
| GET    | `/api/categories`      | USER,CLERK,ADMIN | ―                    | `CategoryResponseDto[]` |
| GET    | `/api/categories/{id}` | USER,CLERK,ADMIN | ―                    | `CategoryResponseDto`   |
| POST   | `/api/categories`      | CLERK,ADMIN      | `CategoryRequestDto` | `CategoryResponseDto`   |
| PUT    | `/api/categories/{id}` | CLERK,ADMIN      | `CategoryRequestDto` | `CategoryResponseDto`   |
| DELETE | `/api/categories/{id}` | CLERK,ADMIN      | ―                    | 204 No Content          |

### Products

| Method | Path                                 | Roles            | Body                | Response               |
| ------ | ------------------------------------ | ---------------- | ------------------- | ---------------------- |
| GET    | `/api/products`                      | USER,CLERK,ADMIN | ―                   | `ProductResponseDto[]` |
| GET    | `/api/products/{id}`                 | USER,CLERK,ADMIN | ―                   | `ProductResponseDto`   |
| POST   | `/api/products`                      | CLERK,ADMIN      | `ProductRequestDto` | `ProductResponseDto`   |
| PUT    | `/api/products/{id}`                 | CLERK,ADMIN      | `ProductRequestDto` | `ProductResponseDto`   |
| DELETE | `/api/products/{id}`                 | CLERK,ADMIN      | ―                   | 204 No Content         |
| GET    | `/api/products/barcode/{code}`       | USER,CLERK,ADMIN | ―                   | `ProductResponseDto`   |
| GET    | `/api/products/barcode/{code}/image` | USER,CLERK,ADMIN | ―                   | image/png (barcode)    |

### Customers

| Method | Path                  | Roles       | Body                 | Response                |
| ------ | --------------------- | ----------- | -------------------- | ----------------------- |
| GET    | `/api/customers`      | CLERK,ADMIN | ―                    | `CustomerResponseDto[]` |
| GET    | `/api/customers/{id}` | CLERK,ADMIN | ―                    | `CustomerResponseDto`   |
| POST   | `/api/customers`      | CLERK,ADMIN | `CustomerRequestDto` | `CustomerResponseDto`   |
| PUT    | `/api/customers/{id}` | CLERK,ADMIN | `CustomerRequestDto` | `CustomerResponseDto`   |
| DELETE | `/api/customers/{id}` | CLERK,ADMIN | ―                    | 204 No Content          |

### Invoices

| Method | Path                              | Roles            | Body                                                          | Response                  |
| ------ | --------------------------------- | ---------------- | ------------------------------------------------------------- | ------------------------- |
| POST   | `/api/invoices`                   | CLERK,ADMIN      | `InvoiceRequestDto` → includes `userId`, `taxRate`, `items[]` | `InvoiceResponseDto`      |
| GET    | `/api/invoices`                   | USER,CLERK,ADMIN | query `userId` (clerk/admin)                                  | `InvoiceResponseDto[]`    |
| GET    | `/api/invoices/{id}`              | USER,CLERK,ADMIN | ―                                                             | `InvoiceResponseDto`      |
| GET    | `/api/invoices/{id}/pdf`          | USER,CLERK,ADMIN | ―                                                             | application/pdf           |
| GET    | `/api/invoices/{id}/email?email=` | CLERK,ADMIN      | (email address)                                               | 200 OK “Invoice sent to…” |

### Analytics (Admin only)

| Method | Path                           | Roles | Query Params                             | Response                                  |
| ------ | ------------------------------ | ----- | ---------------------------------------- | ----------------------------------------- |
| GET    | `/api/analytics/monthly-sales` | ADMIN | `?month=YYYY-MM`                         | `double` (total sales)                    |
| GET    | `/api/analytics/revenue-chart` | ADMIN | `?from=yyyy-MM-dd&to=yyyy-MM-dd`         | `Map<LocalDate,Double>`                   |
| GET    | `/api/analytics/top-products`  | ADMIN | `?from=yyyy-MM-dd&to=yyyy-MM-dd&limit=5` | `List<{ productId, name, quantitySold }>` |

---

## Future Work

* **Multi-tenant** support
* **Role-based UI flags** (feature toggles)
* **Export to Excel / CSV**
* **Real-time stock alerts**
* **Payment gateway integration**
* **Webhooks / event-driven notifications**

---

*© 2025 NidhiSync.*

```

Feel free to tweak naming, add or remove fields, and flesh out any domain-specific details (tax rules, payment status, etc.). This should give your front-end developers a complete, copy-and-paste–ready guide to every API in the system.
```
