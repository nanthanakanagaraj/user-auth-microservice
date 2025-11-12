**user-auth-microservice**

# Project Overview
This project is a Spring Boot microservice designed to handle basic user authentication functionalities.  
It includes APIs for:
- Register  
- Login  
- Logout  
- Reset Password  

The application also implements logging, global exception handling, reusable utility code, and JUnit test cases for all APIs.  
Two developers worked collaboratively through Git branching and merging workflows.

# Tech Stack
- Java 17
- Spring Boot 3+
- Spring Web
- Spring Data JPA
- H2/MySQL Database
- Lombok
- Slf4j Logging
- JUnit 5 + MockMvc
- Swagger (OpenAPI 3)
- AWS EC2 (for deployment)

# Project Structure
src
├── main
│ ├── java/com/example/userauthmicroservice
│ │ ├── controller
│ │ ├── service
│ │ ├── repository
│ │ ├── dto
│ │ ├── model
│ │ ├── exception
│ │ ├── common (utility classes)
│ │ └── config
│ └── resources
│ ├── application.properties
│ └── logback-spring.xml
└── test
└── java/com/example/userauthmicroservice

# Features & Endpoints
| API Endpoint         | HTTP Method| Description          | Developer|
|------------------    |------------|--------------------  |----------|
| `/api/auth/register` | POST       | Registers a new user | A        |
| `/api/auth/login`    | POST       | Authenticates a user | A        |
| `/api/auth/logout`   | POST       | Logs out a user      | B        |
| `/api/auth/reset`    | POST       | Resets user password | B        |

# Development Workflow
Branching
bash
master
├── devA_register_login
└── devB_logout_reset

# Logging
-Logging implemented using @Slf4j (Lombok).
-Logs stored in:logs/user-auth.log

# Global Exception Handling
-A centralized GlobalExceptionHandler is implemented to handle:
  -CustomException
  -ValidationException
  -RuntimeException
-Ensures clean and consistent error responses across APIs.

# JUnit Test Cases
-Test cases are created for all APIs under src/test/java.
API	              Test Method	           Framework
Register	      testRegisterUser()	  JUnit + MockMvc
Login	          testLoginUser()	      JUnit + MockMvc
Logout	        testLogoutUser()	    JUnit + MockMvc
Reset Password	testResetPassword()	  JUnit + MockMvc
-To run tests : mvn test

# Swagger Integration 
- Added Springdoc OpenAPI dependency for Swagger support.
- Configured project details and annotations for all controllers.
- Swagger UI available at: http://localhost:8080/swagger-ui.html
- Displays and allows testing of all API endpoints.

# AWS Deployment 
- Created an AWS account and explored key services like EC2, S3, RDS, IAM, and CloudWatch.
- Launched an EC2 instance and opened port 8080.
- Installed Java and Maven, cloned the GitHub repo, built the project, and ran the JAR file.
- Accessed the application via : http://<EC2-Public-IP>:8080/swagger-ui.html

# Contributors
Name	                Role	                   Branch
Developer A	    Register & Login APIs	    devA_register_login
Developer B	    Logout & Reset APIs	      devB_logout_reset
