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
Using the datsource bean it creates the bean of entitmanager factory and using the entity manager factory bean it creates the bean of entity manager which is reposible for performing the database operation. Howeever, we dont have to do these configuration manually 




