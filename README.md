# Library Management API - Book Tracking System

Library Management API is a Java Spring Boot backend project designed to manage the core operations of a library system. It allows users to add, update, search, and delete books, manage library members, track book availability, issue books to members, return borrowed books, and identify overdue loans. The project uses REST APIs for communication, MySQL for data storage, and JPA/Hibernate for database operations.

## Tech Stack

- Java 17
- Spring Boot 
- JPA / Hibernate
- REST API
- MySQL
- Maven

## Features

- Book CRUD with ISBN uniqueness
- Search books by title, author, ISBN, category, and availability
- Member CRUD with email uniqueness
- Activate or suspend members
- Borrow books with inventory tracking
- Return books and restore available copies
- List all loans, active loans, returned loans, and overdue loans
- Validation and structured JSON error responses


## Project Structure

```text
src/main/java/com/example/library
  book      Book entity, DTOs, repository, service, controller
  member    Member entity, DTOs, repository, service, controller
  loan      Loan entity, DTOs, repository, service, controller
  common    Error response and exception handling
```


## How to Run
- Go to project folder:
- cd C:\Users\iamsu\Documents\Codex\2026-06-07\library-management-api
- Run the Aplication or mvn spring-boot:run
- Tomcat Server started

## How to Test API
-	Use Postman
- Use URL: http://localhost:8080

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
  "author": "Sunny",
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
  "phone": "+91-9876540"
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

