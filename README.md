# dogGO-backend
## Database schema
Current database schema is available at [this address](https://dbdiagram.io/d/5fa669fb3a78976d7b7ae66a). 
## Services
### discovery-service
### gateway-service
### auth-service
To import Keycloak realm config without overriding already existing one,
add `-Dkeycloak.migration.strategy=IGNORE_EXISTING` VM option in `AuthServiceApplication -> Edit configurations`. 
### mail-service
For mail sending to work properly, set correct `spring.mail.password` property in `application.yml`.
