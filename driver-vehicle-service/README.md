# Driver and Vehicle Service

This Spring Boot service manages RideLink drivers and vehicles.

## Configuration

The service connects to SQL Server. Configure `DB_PASSWORD` and `JWT_SECRET` in
the environment before starting it. `DB_HOST`, `DB_PORT`, and `DB_USERNAME`
default to `localhost`, `1433`, and `sa`, respectively. Use a strong,
environment-specific value for `JWT_SECRET`.

Run the service from this directory with:

```text
mvn spring-boot:run
```

Tests use an in-memory H2 database and do not require SQL Server credentials.
