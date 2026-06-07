# Library Management API - Book Tracking System

A complete Java Spring Boot backend for managing books, members, borrowing, returns, availability, and overdue tracking.

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA / Hibernate
- MySQL
- Maven
- Bean Validation

## Features

- Book CRUD with ISBN uniqueness
- Search books by title, author, ISBN, category, and availability
- Member CRUD with email uniqueness
- Activate or suspend members
- Borrow books with inventory tracking
- Return books and restore available copies
- List all loans, active loans, returned loans, and overdue loans
- Validation and structured JSON error responses
- MySQL Docker Compose file
- H2-backed test profile

## Project Structure

```text
src/main/java/com/example/library
  book      Book entity, DTOs, repository, service, controller
  member    Member entity, DTOs, repository, service, controller
  loan      Loan entity, DTOs, repository, service, controller
  common    Error response and exception handling
```

## Run Locally

Start MySQL:

```bash
docker compose up -d
```

Run the application:

```bash
mvn spring-boot:run
```

The API runs at:

```text
http://localhost:8080
```

Default database settings are in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_management?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=password
```

You can override the database values from IntelliJ or the terminal without editing the file:

```text
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
```

In IntelliJ, open the run configuration for `LibraryManagementApiApplication`, then add those values under **Environment variables**.

If the console says `Access denied for user 'root'@'localhost'`, the app reached MySQL, but MySQL rejected the username/password.

## API Endpoints

### Books

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/books` | List books. Optional filters: `keyword`, `category`, `available` |
| GET | `/api/books/{id}` | Get one book |
| POST | `/api/books` | Create a book |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book if no copies are borrowed |

Example book body:

```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "category": "Software Engineering",
  "totalCopies": 3
}
```

### Members

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/members` | List members |
| GET | `/api/members/{id}` | Get one member |
| GET | `/api/members/{id}/loans/active` | List active loans for a member |
| POST | `/api/members` | Create a member |
| PUT | `/api/members/{id}` | Update a member |
| PATCH | `/api/members/{id}/status` | Update member status |
| DELETE | `/api/members/{id}` | Delete member if they have no active loans |

Example member body:

```json
{
  "name": "Asha Sharma",
  "email": "asha@example.com",
  "phone": "+91-9876543210"
}
```

Status update body:

```json
{
  "status": "SUSPENDED"
}
```

Allowed statuses: `ACTIVE`, `SUSPENDED`.

### Loans

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/loans` | List loans. Optional filters: `status`, `overdue` |
| GET | `/api/loans/{id}` | Get one loan |
| POST | `/api/loans/borrow` | Borrow a book |
| POST | `/api/loans/{id}/return` | Return a book |

Borrow body:

```json
{
  "bookId": 1,
  "memberId": 1,
  "loanDays": 14
}
```

Allowed loan statuses: `BORROWED`, `RETURNED`.

## Error Response Format

```json
{
  "timestamp": "2026-06-07T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed.",
  "path": "/api/books",
  "validationErrors": {
    "title": "Title is required."
  }
}
```

## Test

```bash
mvn test
```

## Sample Requests

Open `http-requests.http` in IntelliJ IDEA, VS Code REST Client, or any compatible HTTP client to try the API quickly.
