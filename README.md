# Timer Backend

Timer Backend is a robust Spring Boot-based REST API designed for a time-tracking application. It allows users to track their activities, manage labels for categorization, and customize their timer settings.

## 🚀 Features

- **User Authentication**: Secure registration and login using JWT (JSON Web Tokens).
- **Time Tracking**: Create, update, and manage timer entries with start times, end times, and durations.
- **Label Management**: Categorize your timer entries with custom labels.
- **Timer Settings**: Personalize your timer experience with user-specific settings and options.
- **Data Export/Import**: Export your timer history to CSV and import it back when needed.
- **Rate Limiting**: Protect your API from abuse with built-in rate limiting.
- **API Documentation**: Interactive API documentation powered by Swagger/OpenAPI.
- **Database Migrations**: Reliable database schema management using Liquibase.

## 🛠 Tech Stack

- **Framework**: Spring Boot 4.x (based on pom.xml)
- **Language**: Java 17
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT (JJWT)
- **Object Mapping**: MapStruct
- **Database Migration**: Liquibase
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Utilities**: Lombok, Checkstyle, Jacoco
- **Containerization**: Docker

## 📋 Prerequisites

Before you begin, ensure you have the following installed:
- [Java 17+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)
- [PostgreSQL](https://www.postgresql.org/) (if running locally)

## 🏗 Setup & Installation

### 1. Database Configuration
By default, the application connects to a PostgreSQL database at `localhost:5432/postgres`.
You can adjust these settings in `src/main/resources/application-dev.yaml`.

### 2. Run Database Migrations
Liquibase is used for database migrations. To drop all tables and start fresh:
```bash
liquibase drop-all \
    --url=jdbc:postgresql://localhost:5432/postgres \
    --username=postgres \
    --password=password
```

### 3. Build the Application
```bash
./mvnw clean package
```

### 4. Run the Application
```bash
./mvnw spring-boot:run
```
The application will be available at `http://localhost:8080`.

## 🐳 Running with Docker

### Build Image
```bash
./mvnw spring-boot:build-image
```

### Run Container
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/postgres \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=password \
  timer_backend:0.0.1-SNAPSHOT
```

## 📚 API Documentation

Once the application is running, you can access the interactive Swagger UI at:
`http://localhost:8080/swagger-ui/index.html`

## 🔌 Core API Endpoints

- `POST /api/v1/auth/register` - User registration
- `POST /api/v1/auth/login` - User login
- `GET/POST/PUT/DELETE /api/v1/timer-entries` - Timer entry management
- `GET /api/v1/timer-entries/export` - Export timer entries (CSV)
- `POST /api/v1/timer-entries/import` - Import timer entries
- `GET/POST/PUT/DELETE /api/v1/labels` - Label management
- `GET/POST/PUT/DELETE /api/v1/timer-settings` - User timer settings
- `GET/POST/PUT/DELETE /api/v1/timer-options` - Predefined timer options
- `GET/PUT /api/v1/users/me` - User profile management