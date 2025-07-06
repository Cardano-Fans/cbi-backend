# CBI Backend Service

A production-ready Spring Boot microservice for transaction counting and aggregation using Yaci-Store with 30-day data pruning.

## Features

- **Transaction Counting**: Count transactions by date and epoch
- **Data Aggregation**: Daily and epoch-level transaction aggregation
- **Minimal Database Footprint**: Uses only Yaci-Store core with event-driven aggregation
- **Event-Driven Architecture**: Listens to Yaci-Store TransactionEvent via Spring Event Bus
- **REST API**: Clean REST endpoints for transaction count queries
- **Production Ready**: Docker support, monitoring, health checks, and comprehensive logging
- **Performance Optimized**: PostgreSQL with proper indexing and connection pooling

## Architecture

- **Backend**: Spring Boot 3.5.0 with Java 24
- **Database**: PostgreSQL with Yaci-Store schema
- **Blockchain Integration**: Yaci-Core for Cardano node connectivity
- **Data Stores**: Minimal Yaci-Store core with event processing
- **Build**: Maven with Lombok for clean code

## Quick Start

### Prerequisites

- Java 24 or later
- Maven 3.9+ or use included wrapper
- PostgreSQL database
- Cardano Node (for Yaci-Store synchronization)

### Environment Setup

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd cbi-backend-service
   ```

2. **Configure database and Cardano node connection** in `application.properties`

3. **Build and run**:
   ```bash
   mvn clean package
   java -jar target/cbi-backend-service-1.0.0.jar
   ```

### Docker Deployment

```bash
# Build and run with Docker Compose
docker-compose up -d

# Services available:
# - CBI Microservice: localhost:8080
# - PostgreSQL: localhost:5433
# - Prometheus: localhost:9090
# - Grafana: localhost:3000
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | localhost | PostgreSQL host |
| `DB_PORT` | 5433 | PostgreSQL port |
| `DB_NAME` | yaci_indexer | Database name |
| `DB_SCHEMA` | mainnet | Schema name |
| `DB_USERNAME` | user | Database username |
| `DB_PASSWORD` | | Database password |
| `CARDANO_NODE_HOST` | localhost | Cardano node host |
| `CARDANO_NODE_PORT` | 3001 | Cardano node port |
| `CARDANO_PROTOCOL_MAGIC` | 764824073 | Protocol magic number |
| `EVENTS_ENABLED` | true | Enable event processing |
| `TRANSACTION_EVENTS` | true | Enable transaction events |

### Yaci-Store Configuration

Minimal core configuration:
- Core Store (basic block and transaction events)
- Event Bus integration for real-time processing
- No additional stores for minimal database footprint

## API Endpoints

### Transaction Count Endpoints

- `GET /api/v1/transactions/count/date/{date}` - Get transaction count for specific date
- `GET /api/v1/transactions/count/date-range?startDate={start}&endDate={end}` - Get counts for date range
- `GET /api/v1/transactions/count/epoch/{epoch}` - Get transaction count for specific epoch
- `GET /api/v1/transactions/count/epoch-range?startEpoch={start}&endEpoch={end}` - Get counts for epoch range

### Data Refresh Endpoints

- `POST /api/v1/transactions/reset/date-range?startDate={start}&endDate={end}` - Reset day counts to zero
- `POST /api/v1/transactions/reset/epoch-range?startEpoch={start}&endEpoch={end}` - Reset epoch counts to zero

### Monitoring Endpoints

- `GET /actuator/health` - Health check
- `GET /actuator/metrics` - Application metrics
- `GET /actuator/prometheus` - Prometheus metrics

## Response Format

All count endpoints return JSON in the format:
```json
{
  "2024-01-15": 12345,
  "2024-01-16": 23456
}
```

Where keys are dates/epochs and values are transaction counts.

## Database Schema

### Application Tables
- `transaction_day_count` - Daily transaction count aggregations
- `transaction_epoch_count` - Epoch transaction count aggregations

### Yaci-Store Tables
The service uses minimal Yaci-Store core tables:
- Core event tables for blockchain synchronization
- No persistent transaction or block storage
- Event-driven processing only

## Performance Features

### Database Optimizations
- **Indexes**: Optimized indexes on date and epoch columns
- **Connection Pooling**: HikariCP with tuned settings
- **Event Processing**: Real-time transaction event handling

### Application Optimizations
- **Spring Cache**: In-memory caching for count data
- **Lombok**: Clean code with generated getters/setters
- **Event-Driven**: Real-time processing without persistent storage overhead

## Monitoring

### Metrics
- Prometheus metrics export
- JVM and application metrics
- Database connection pool metrics

### Health Checks
- Spring Actuator health endpoint
- Database connectivity check
- Yaci-Store synchronization status

### Logging
- Structured logging with configurable levels
- Request/response logging
- Database query logging (dev mode)

## Development

### Build Commands
```bash
# Clean build
mvn clean package

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Skip tests
mvn clean package -DskipTests
```

### Profiles
- `dev` - Development with debug logging
- `prod` - Production optimized

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check database credentials in `application.properties`
   - Verify PostgreSQL is running
   - Ensure Yaci-Store schema exists

2. **Cardano Node Connection Failed**
   - Verify Cardano node is running and accessible
   - Check protocol magic and port settings
   - Ensure node is synchronized

3. **High Memory Usage**
   - Adjust JVM heap settings: `-Xmx1g -Xms512m`
   - Tune connection pool size
   - Check event processing load

### Logs and Debugging

```bash
# View application logs
tail -f logs/cbi-backend-service.log

# Check health status
curl http://localhost:8081/actuator/health

# View metrics
curl http://localhost:8081/actuator/metrics

# Test transaction count API
curl http://localhost:8080/api/v1/transactions/count/date/2024-01-15
```

## License

This project is licensed under the MIT License.