## Requirements

#### Application
- Java 11
- Maven

#### Database
- PostgreSQL

## Start locally
```
mvn clean install
java <env variables: -DvariableName='value'> -jar <jar location>
```
## Start locally for development
```
mvn compile
mvn <env variables: -DvariableName='value'> spring-boot:run
```

## Environment variables:

- DB_URL (_default_: 'jdbc:postgresql://localhost:5432/pnu_feedback') - _db url_
- DB_USERNAME (_default_: 'postgres') - _db username_
- DB_PASSWORD (_default_: 'root') - _db password_
- ADMIN_USERNAME (_default_: 'admin') - _admin panel username_
- ADMIN_PASSWORD (_default_: 'admin') - _admin panel password_
- JWT_SECRET (_default_: 'secret') - _jwt secret, that is used to sign tokens for feedback submissions_
- APP_BASE_URL (_default_: 'http://localhost:8080') - _application base url_
- ADMIN_PANEL_URL (_default_: 'admin') - _path to admin panel_