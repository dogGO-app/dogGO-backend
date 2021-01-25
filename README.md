# dogGO-backend

## Database schema
Current database schema is available at [this address](https://dbdiagram.io/d/5ff8543780d742080a358c9d). 

## Services

### discovery-service
After starting this service Eureka dashboard is available at http://localhost:8761 for default config.

### gateway-service
After starting this service API documentation is available at http://localhost:8080/swagger-ui.html for default config.

### auth-service
Run `docker-compose up -d` to initialise database before starting this service.

To import Keycloak realm config without overriding already existing one,
add `-Dkeycloak.migration.strategy=IGNORE_EXISTING` VM option in `AuthServiceApplication -> Edit configurations` in IntelliJ. 

### mail-service
For mail sending to work properly, use default config or set correct mail account properties in `application.yml`. 

### dog-lover-service
Run `docker-compose up -d` to initialise database before starting this service.

## Copyright notice
Copyright © 2021 Michał Najborowski, Adrian Glapiński, Marta Tarka, Radosław Leszkiewicz. All rights reserved.
