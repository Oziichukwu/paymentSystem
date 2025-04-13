## Family Payment System

A secure real-time payment processing system for a family-oriented service built using Java Spring Boot.

## 🚀 Features

Atomic multi-table updates for parent and student balances.

Non-trivial payment logic with dynamic fee calculations.

Role-based access control using Spring Security.

H2 in-memory database with seed data.

RESTful endpoint to process payments.

## 📦 Technologies
<ul>
<li>Maven:  For dependency management and build automation
<li>H2 Database: In-memory database for development and testing 
<li>Java 17
<li>Spring Version 3.3.4
<li>Coding IDE (IntelliJ IDEA preferably)
</ul> 

<b>You can download and install java from Oracle's Java website <code> https://www.oracle.com/java/technologies/downloads/#java21?er=221886 </code> <b>

# Database Configuration:
<b>Setup H2 Database</b> <br>
spring.datasource.url=jdbc:h2:mem:testdb <br>
spring.datasource.driverClassName=org.h2.Driver <br>
spring.datasource.username=sa <br>
spring.datasource.password=password <br>
### JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect <br>
spring.jpa.show-sql=true <br>
spring.jpa.hibernate.ddl-auto=update <br>

### H2 Console
spring.h2.console.enabled=true <br>
spring.h2.console.path=/h2-console <br>

The database configuration details above should be included in your application.properties file. <br>


# How to Run the Application
### Step 1. clone the Project
<b>1. git clone https://github.com/your-username/family-payment-system.git </b> <br>
<b>2. cd family-payment-system </code> </b> <br>
<b>3. Navigate to the extracted folder. This folder should contain the core files of the Spring Boot project like pom.xml (Maven) along with source code in a src folder </b> <br><br>

### Step 2. Open the Project in Intellij IDEA or any IDE
<b>1. Launch IntelliJ IDEA: Open IntelliJ IDEA from your system. </b> <br>
<b>2. Click on Open or Open Project from the welcome screen. </b> <br>
<b>3. Navigate to the folder where you unzipped the project and select it. </b> <br>
<b>4. IntelliJ will recognize the Maven configuration files (pom.xml) and automatically import the project dependencies. Wait for the project to load successfully. </b> <br>
<b>5. Make sure the project is using the correct JDK version. Go to File → Project Structure → Project Settings → Project. Under "Project SDK", set it to the correct JDK version (Java 27). </b> <br>


### Step 3. Build the Project
<b>1. Open the terminal in IntelliJ (bottom of the window) and run: </b> <code> mvn clean install </code>  <br>
<b>This will download all the required dependencies, compile the project, and run all tests (unit test on the project) . </b>  <br>

### Step 4. Run the Spring boot Application
<b>1. Open a terminal or command prompt, navigate to the project root folder (the one with pom.xml). </b> <br>
<b>2. Now run:  </b>  <code> mvn spring-boot:run </code> <br>
<b>3. This will start the Spring Boot application. Similar to the IntelliJ logs, you'll see the server start message in the terminal. </b>  <br>

### Step 5: Access the Application
<b>1. Open your browser and navigate to http://localhost:8080  </b> <br>
<b>2. You can test the endpoints using Postman, with the domain/port:  http://localhost:8080  </b> <br>


## 🔐 Authentication & Roles

HTTP Role Based Authentication enabled.

Only users with the ROLE_ADMIN authority can process payments.


## 📡 API Endpoint

POST /api/auth

<b>Request Body to Register a user "/register"</b> <br>
<b>{
"username": "user",
"password": "password",
"roles": [
"USER"
]
} </b> <br>

<b>Request Body to Login user "/login"</b> <br>
<code>{
"username": "user",
"password": "password"
} </code> <br><br>
<b>After Login A token is generated which can be used to then process payment  </b> <br>


POST /api/payments

<b>Request Body to process payment </b> <br>
<code>{
{
"parentId": 3,
"studentId": 2,
"paymentAmount": 311.00
}
} </code>

