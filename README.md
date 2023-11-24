# USSD Mobile Banking API

## Overview
This is a USSD mobile banking API that allows users to perform banking transactions.
It is built using Java, Spring Boot, and Maven.

## Features
- User registration
- User account balance
- User account statement
- Account deposit
- Account withdrawal
- Account transfer

## Technologies
- Java
- Spring Boot
- Maven

## Setup
- Clone the repository
- Open the project in your IDE
- Run the project

## API Endpoints

### 1. GET /api/v1.2/users/language

This endpoint is used to fetch languages. It accepts the "Accept-Language" header to determine the locale.

Example:
```http
GET /api/v1.9/users/language HTTP/1.1
Host: localhost:8080
Accept-Language: en_US
```

### 2. POST /api/v1.2/users
This endpoint is used to fetch a specific account by its number. It returns the account details for the given account number.

Example:
```http
GET /api/v1.2/users/accounts/123456 HTTP/1.1
Host: localhost:8080
```

### 3. POST /api/v1.2/users/accounts
This endpoint is used to fetch a specific account by its number. It returns the account details for the given account number.

Example:
```http
GET /api/v1.2/users/accounts/123456 HTTP/1.1
Host: localhost:8080
```

### 4. POST /api/v1.2/users/accounts/123456/deposit
This endpoint is used to fetch a specific account by its number. It returns the account details for the given account number.

Example:
```http
GET /api/v1.2/users/accounts/123456 HTTP/1.1
Host: localhost:8080
```

### 5. POST /api/v1.2/users/accounts/123456/withdraw
This endpoint is used to fetch a specific account by its number. It returns the account details for the given account number.

Example:
```http
GET /api/v1.2/users/accounts/123456 HTTP/1.1
Host: localhost:8080
```

### 6. POST /api/v1.2/users/accounts/123456/transfer
This endpoint is used to fetch a specific account by its number. It returns the account details for the given account number.

Example:
```http
GET /api/v1.2/users/accounts/123456 HTTP/1.1
Host: localhost:8080
```

### 7. DELETE /api/v1.2/users/accounts/{accountNum}
This endpoint is used to fetch a specific account by its number. It returns the account details for the given account number.

Example:
```http
GET /api/v1.2/users/accounts/123456 HTTP/1.1
Host: localhost:8080
```