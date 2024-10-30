# RDS-Backend

## Introduction

The Backend for Regional-Delicacy-Shop Application, which serves RESTful APIs for the React app. Here are the links:

- Backend URL: https://api.regionaldelicacyshop.software
- API documentation in Swagger UI: https://api.regionaldelicacyshop.software/swagger-ui/index.html

## Technology Stack

- Back-end: Java Spring Boot
- Database: PostgreSQL

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) 21 or later
- Apache Maven (Or use the Maven wrapper which is already added in this project)
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

## Getting Started

To install and run the application, follow these steps:

1. **Clone the repository:**

```sh
git clone https://daiquydev@dev.azure.com/daiquydev/Regional-Delicacy-Shop/_git/RDS-Backend
cd RDS-Backend
```

2. **Add the environment variables:**

You have to add the environment variables specified in `.env.example` file (Please ask the owner for more details of the actual values). You might want to add a local `.env` file for better management of the variables. However, currently the app does not support reading the `.env` file, so you have to add them manually in the terminal, or use your IDE extensions/tools to automatically read them.

3. **Start the application:**

```sh
mvn spring-boot:run
```

The application will start running at http://localhost:8080. The dependencies will be installed from the Maven repo if they are not found on your local machine.

If you are using VS Code, you should install [Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=vmware.vscode-boot-dev-pack) and use the **Spring Boot Dashboard** to start the app instead. It will automatically read `.env` file. If you change the env, you will have to restart the app.

## Build and Test

To build and package the app, run the following command:

```sh
mvn clean package -DskipTests
```

To run the tests, configure the environment to use H2 database and run:

```sh
mvn test
```

## Maven Configuration

The pom.xml file contains all the necessary dependencies and plugins for the Spring Boot application. Key dependencies include:

- spring-boot-starter-web: For building web applications
- spring-boot-starter-data-jpa: For database operations using Spring Data JPA
- spring-boot-starter-validation: For basic validation
- postgresql: For the PostgreSQL driver
- spring-boot-starter-test: For testing
- springdoc-openapi-starter-webmvc-ui: For generating Swagger UI API documentation. The API doc helps you to explore all the APIs of the backend. You can also use it to test API directly. (For more information please read the [Deployment](#deployment) section)

To add new dependencies, simply add them to the `<dependencies>` section of the pom.xml file.

## Deployment

The application is currently deployed on AWS (Amazon Web Service) Elastic Beanstalk with 1 underlying EC2 instance.

## Contribute

If you want to contribute to this project, please follow these steps:

1. Fork the project
2. Create your feature branch (git checkout -b feature/AmazingFeature)
3. Commit your changes (git commit -m 'Add some AmazingFeature')
4. Push to the branch (git push origin feature/AmazingFeature)
5. Open a pull request

## Support:

For issues or questions, please contact our support team.
