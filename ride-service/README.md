# Ride Management Service

Ride Management Service is a backend microservice of the RideLink system.

It is responsible for creating and managing rides, assigning available drivers, and controlling the ride status lifecycle.

## Technologies

- Java 17
- Spring Boot
- Spring Data JPA
- SQL Server
- REST API
- Swagger / OpenAPI
- Maven
- JUnit
- Mockito

## Service Port

The Ride Management Service runs on:

```text
http://localhost:8083
```

## Database

Database used:

```text
RideLinkDB
```

The database password is provided using the `DB_PASSWORD` environment variable and is not stored directly in the source code.

## Ride Status Lifecycle

The supported ride lifecycle is:

```text
REQUESTED
   ↓
ASSIGNED
   ↓
ACCEPTED
   ↓
IN_PROGRESS
   ↓
COMPLETED
```

A ride can also be cancelled when the lifecycle rules allow it.

Invalid status transitions are rejected by the service.

## REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/rides` | Create a new ride |
| GET | `/api/rides` | Get all rides |
| GET | `/api/rides/{id}` | Get a ride by ID |
| PUT | `/api/rides/{id}` | Update a ride |
| DELETE | `/api/rides/{id}` | Delete a ride |
| PUT | `/api/rides/{id}/assign-driver` | Assign an available driver |
| GET | `/api/rides/test` | Test the Ride Service |

## Driver Service Integration

The Ride Management Service communicates with the Driver & Vehicle Service.

Driver & Vehicle Service:

```text
http://localhost:8082
```

Available-driver endpoint:

```text
GET /api/drivers/available
```

When:

```text
PUT /api/rides/{id}/assign-driver
```

is called, the Ride Management Service requests an available driver from the Driver & Vehicle Service.

If a driver is available:

1. The driver ID is assigned to the ride.
2. The ride status changes from `REQUESTED` to `ASSIGNED`.
3. The updated ride is saved in the database.

## Swagger UI

When the Ride Management Service is running, Swagger UI is available at:

```text
http://localhost:8083/swagger-ui/index.html
```

## Run the Service

Make sure SQL Server is running and `RideLinkDB` exists.

Run:

```bash
DB_PASSWORD='YOUR_DATABASE_PASSWORD' mvn spring-boot:run
```

Do not store the real database password in the repository.

## Run Unit Tests

Run:

```bash
mvn test
```

The Ride Management Service currently contains unit tests for:

- Ride creation
- Valid ride status transitions
- Invalid ride status transitions
- Completed ride transition protection
- Available driver assignment
- No available driver handling

## Integration Flow

```text
Client / Postman
       |
       v
Ride Management Service
       |
       | GET /api/drivers/available
       v
Driver & Vehicle Service
       |
       v
Available Driver
       |
       v
Ride updated to ASSIGNED
```

## API Testing

The REST APIs can be tested using:

- Swagger UI
- Postman
- cURL