# Yoga App

This project is a full-stack application with a Java (Spring Boot) back-end and an Angular front-end.

## Prerequisites

- Node.js (recommended: 14.x or 16.x)
- npm
- Java 11 or higher
- Maven

## Installation

Clone the repository:

```sh
git clone https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing
cd Testez-une-application-full-stack
```

### Back-end installation
```sh
cd back
mvn clean install
```

### Front-end installation
```sh
cd ../front
npm install
```

## Running the Application

Start the back-end
in the back folder:
```sh
mvn spring-boot:run
```

The back-end will be available at http://localhost:8080.

Start the front-end
in the front folder:
```sh
npm run start
```

The front-end will be available at http://localhost:4200.

## Running Tests

### Front-end unit tests
In the front folder:
```sh
npm run test
```

to run tests un watch mode:
```sh
npm run test:watch
```

### End-to-end(E2E) tests
In the front folder:
```sh
npm run e2e
```

### Cypress GUI
You can also use Cypress to visualize e2e tests

In one terminal:
```sh
npm run start
```

in other terminal
```sh
npm run cypress:open
```

To generate an E2E coverage report:
```sh
npm run e2e:coverage
```

The report will be available at front/coverage/lcov-report/index.html

### Back-end code coverage
In the back folder:
```sh
mvn clean test
```

The jacoco report will be generated at back/target/site/jacoco/index.html

## Resources

- Postman collection: ressources/postman/yoga.postman_collection.json
- SQL database creation script: ressources/sql/script.sql

### Default admin account:

- login: yoga@studio.com
- password: test!1234