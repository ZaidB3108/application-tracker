# Application Tracker

Personal application tracking dashboard built with Angular, Spring Boot, and PostgreSQL.

## Structure

- `frontend/` — Angular client
- `backend/` — Spring Boot REST API

## Run locally

1. Ensure your local PostgreSQL service is running. The API is configured for the existing `applyflow_db` database.
2. Set `DB_USERNAME` and `DB_PASSWORD` if they differ from the defaults in `backend/src/main/resources/application.properties` (currently `postgres` / `postgres`).
3. Start the API from `backend`: `mvn spring-boot:run`.
4. Start the client from `frontend`: `npm install` then `npm start`.

The client is served on `http://localhost:4200` and the API on `http://localhost:8081`.
