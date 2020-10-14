# dogGO-backend
## Services
### discovery-service
### gateway-service
### auth-service
To import Keycloak realm config without overriding already existing one,
add `-Dkeycloak.migration.strategy=IGNORE_EXISTING` VM option in `AuthServiceApplication -> Edit configurations`. 
### mail-service
For mail sending to work properly, set correct `spring.mail.password` property in `application.yml`.