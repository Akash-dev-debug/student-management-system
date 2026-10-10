
## JDBC Connection Pooling

This project uses Apache Commons DBCP2 to manage database connections.

### Pool Configuration

- Initial connections: 2
- Maximum connections: 10
- Maximum idle connections: 5
- Minimum idle connections: 2
- Maximum wait time: 5000 milliseconds

### Benefits

- Reuses database connections instead of creating a new connection for every operation.
- Limits the maximum number of pooled connections.
- Reduces connection-management overhead.
- Closes the connection pool when the application exits.

### Implementation

The connection pool is configured in `DatabaseConnection.java`.
Database operations obtain connections through `getConnection()`.
The application closes the pool through `closePool()` during shutdown.
