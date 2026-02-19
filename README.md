Multiple Database Connector using Spring Boot (Oracle + PostgreSQL)

A Spring Boot 3 application demonstrating how to configure and manage multiple relational databases (Oracle and PostgreSQL) within a single application using Spring Data JPA and EntityManager.

This project provides a clean layered architecture and separates database concerns using dedicated configuration classes.

This application connects to:

🟠 Oracle Database

🟢 PostgreSQL Database

Each database has its own database configuration dedicated entity manager separate repositories and indivdual service and controller layer 

The project demonstrates real-world enterprise use cases where different modules interact with different databases.

Architecture is as follows:

com.example.demo
│
├── config
│   ├── OracleDBConfig.java
│   └── PostgresDBConfig.java
│
├── doctor (Oracle DB)
│   ├── controller
│   ├── entity
│   ├── repository
│   └── service
│
├── patient (PostgreSQL DB)
│   ├── controller
│   ├── entity
│   ├── repository
│   └── service
│
└── Day99114MultiDataBaEntityManagerApplication.java

How It Works:
When we create a sring boot application we use spring data JPA which internally takes helps of entity manager. 
Entity manager is reponsible for creating the bean of datasource by taking the database specific credentials from the appplication.properties file.
Using the datsource bean it creates the bean of entitmanager factory and using the entity manager factory bean it creates the bean of entity manager which is reposible for performing the database operation. Howeever, we dont have to do these configuration manually because spring takes care of it using its autoconfiguration feature.

Regardless of that, if you want to create a springboot application where you want tot connect the application to two databases then at that time you will have to manually configure the entity manager.

So, in this project I configured it manually and successfuly connected it to the two Postgres and oracle database. For that i had to create two separate configuration of OracleDBConfig and PostgresDBConfig.

Each configuration includes:

DataSource bean

EntityManagerFactory

TransactionManager

@EnableJpaRepositories with base package segregation

| Technology        | Version          |
| ----------------- | ---------------- |
| Java              | 17               |
| Spring Boot       | 3.5.6            |
| Spring Data JPA   | Included         |
| Oracle JDBC       | ojdbc11          |
| PostgreSQL Driver | Latest           |
| Maven             | Wrapper Included |
| Lombok            | 1.18.30          |

Modules Explained:-

1]Doctor Module (Oracle DB)
Entity: Doctorr
Repository: DoctorRepository
Service: DoctorService
Controller: DoctorController
Database: Oracle

This module persists and retrieves Doctor data from Oracle.


2]Patient Module (PostgreSQL DB)
Entity: Patients
Repository: PatientRepository
Service: PatientService
Controller: PatientController
Database: PostgreSQL
This module interacts exclusively with PostgreSQL.


How to Run the Project:
1] Clone the repository 
2] Ensures you have Postgres and Orcale databse installed
3] Update the credentials in application.yml with your actual database credentials.
4] You will be able to run the program 



