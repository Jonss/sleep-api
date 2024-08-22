# Noom Take home test

## Requirements
- **Docker:** Ensure Docker is installed and running on your system.
- **Java 11:** The project requires Java 11.


## Getting Started
To start the project, you have two options:

- Using Docker Compose: Run the following command to build and start the database and API containers:
```sh
docker-compose up --build
```

- Using Make Command: Alternatively, you can use the provided `make` command:
```sh
make run
```

## Integration tests

To run the repository tests, you'll need a PostgreSQL Docker container. Start the container by executing the following commands:

```sh
docker-compose up --build db_test
```

or 

```sh
make test-env
```

## Testing the API Locally

A Postman collection is available in the `resources/` directory. Import the collection and the associated environment to easily test the API endpoints.

## API endpoints

### 1. Create user

Create a new user in the webserver.

```sh
curl --location 'http://localhost:8080/api/users' \
--header 'Content-Type: application/json'
```

### 2. Create Sleep log

Record a sleep log for a user.

```sh
curl --location 'http://localhost:8080/api/sleep' \
--header 'x-external-id: d4c063d2-9ffe-4af8-b607-87cd7f5ca60c' \
--header 'Content-Type: application/json' \
--data '{
    "startDate": "2024-08-20T21:32:00Z",
    "endDate": "2024-08-21T06:30:00Z",
    "quality": "GOOD"   
}'
```
### 3. Get last night

Retrieve the sleep log for the last night for a specific user.

```sh
curl --location 'http://localhost:8080/api/sleep/last-night' \
--header 'x-external-id: d54a761a-cbc3-451b-ac9a-b89898d7b6f9' 
```

### 4. Get 30 days data

Retrieve sleep logs for the past 30 days for a specific user.

```sh
curl --location 'http://localhost:8080/api/sleep/interval' \
--header 'x-external-id: d54a761a-cbc3-451b-ac9a-b89898d7b6f9'
```

## Additional Information

- Ensure the x-external-id header corresponds to the correct external_id of the user.
- The API runs on localhost:8080 by default, but this can be configured in the Docker setup if needed.
- The Postman environment included in the resources/ directory will help streamline testing by pre-setting common variables.

