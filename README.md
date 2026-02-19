# 🔗 Multiple Database Connector — Spring Boot + Oracle + PostgreSQL

> A clean, production-ready reference implementation of **connecting multiple databases simultaneously** in a single Spring Boot application using **JPA EntityManagers**, without any third-party multi-tenancy libraries.

---

## 📌 Overview

Most real-world enterprise applications need to talk to more than one database. Spring Boot's auto-configuration only wires up a single datasource by default — this project demonstrates exactly how to break out of that limitation.

This application connects to both an **Oracle DB** (for Doctor data) and a **PostgreSQL DB** (for Patient data) at the same time, with each database having its own:

- `DataSource`
- `EntityManagerFactory`
- `TransactionManager`
- `JPA Repository` scope

Each domain module is fully isolated, so changes to one database config never affect the other.

---

## 🏗️ Architecture

```
┌──────────────────────────────────────────────────────────┐
│                   Spring Boot Application                │
│                                                          │
│  ┌─────────────────────┐   ┌─────────────────────────┐  │
│  │     Doctor Module    │   │     Patient Module       │  │
│  │  (Oracle Database)   │   │  (PostgreSQL Database)   │  │
│  │                      │   │                          │  │
│  │  DoctorController    │   │  PatientController       │  │
│  │  DoctorService       │   │  PatientService          │  │
│  │  DoctorRepository    │   │  PatientRepository       │  │
│  │  Doctorr (Entity)    │   │  Patients (Entity)       │  │
│  └──────────┬───────────┘   └────────────┬─────────────┘  │
│             │                            │                │
│  ┌──────────▼───────────┐   ┌────────────▼─────────────┐  │
│  │   OracleDBConfig     │   │   PostgresDBConfig        │  │
│  │  EntityManager (JPA) │   │   EntityManager (JPA)     │  │
│  │  TransactionManager  │   │   TransactionManager      │  │
│  └──────────┬───────────┘   └────────────┬─────────────┘  │
│             │                            │                │
└─────────────┼────────────────────────────┼────────────────┘
              │                            │
       ┌──────▼──────┐             ┌───────▼──────┐
       │  Oracle DB   │             │  PostgreSQL  │
       │  Port: 1521  │             │  Port: 5432  │
       └─────────────┘             └─────────────┘
```

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Core language |
| Spring Boot | 3.5.6 | Application framework |
| Spring Data JPA | (via Boot) | ORM & repository abstraction |
| Hibernate | (via Boot) | JPA implementation |
| Oracle JDBC (`ojdbc11`) | Runtime | Oracle DB connectivity |
| PostgreSQL JDBC | Runtime | PostgreSQL DB connectivity |
| Lombok | 1.18.30 | Boilerplate reduction |
| Maven | 3.x | Build tool |

---

## 📂 Project Structure

```
src/
└── main/
    ├── java/com/example/demo/
    │   ├── Day99114MultiDataBaEntityManagerApplication.java   ← Entry point
    │   │
    │   ├── config/
    │   │   ├── OracleDBConfig.java      ← Oracle DataSource, EntityManager, TxManager
    │   │   └── PostgresDBConfig.java    ← Postgres DataSource, EntityManager, TxManager
    │   │
    │   ├── doctor/                      ← Oracle domain
    │   │   ├── controller/DoctorController.java
    │   │   ├── service/DoctorService.java
    │   │   ├── repository/DoctorRepository.java
    │   │   └── entity/Doctorr.java
    │   │
    │   └── patient/                     ← PostgreSQL domain
    │       ├── controller/PatientController.java
    │       ├── service/PatientService.java
    │       ├── repository/PatientRepository.java
    │       └── entity/Patients.java
    │
    └── resources/
        └── application.yml              ← Dual datasource configuration
```

---

## ⚙️ Configuration

All datasource configuration lives in `application.yml`:

```yaml
server:
  port: 8081

spring:
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update

  datasource:
    postgres:
      jdbc-url: jdbc:postgresql://localhost:5432/postgres
      username: postgres
      password: your_password
      driver-class-name: org.postgresql.Driver

    oracle:
      jdbc-url: jdbc:oracle:thin:@localhost:1521:XE
      username: system
      password: your_password
      driver-class-name: oracle.jdbc.OracleDriver
```

Each datasource is bound to its config class via `@ConfigurationProperties`, keeping credentials cleanly separated.

---

## 🔑 How Multiple Databases Are Wired Up

The key insight in this project is the use of **separate `@Configuration` classes** for each database. Here's how it works:

**1. Each DB gets its own `DataSource` bean:**
```java
@Bean(name = "oracleDataSource")
@ConfigurationProperties(prefix = "spring.datasource.oracle")
public DataSource oracleDataSource() {
    return DataSourceBuilder.create().build();
}
```

**2. Each `DataSource` gets its own `EntityManagerFactory`** that scans only its domain's packages:
```java
@Bean(name = "oracleEntityManagerFactory")
@Primary  // One factory must be @Primary
public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(...) {
    em.setPackagesToScan("com.example.demo.doctor.entity");
    em.setPersistenceUnitName("oracle");
    ...
}
```

**3. Each `EntityManagerFactory` gets its own `TransactionManager`:**
```java
@Bean(name = "oracleTransactionManager")
public PlatformTransactionManager oracleTransactionManager(...) {
    return new JpaTransactionManager(entityManagerFactory);
}
```

**4. Repositories are scoped to the correct factory** via `@EnableJpaRepositories`:
```java
@EnableJpaRepositories(
    basePackages = "com.example.demo.doctor.repository",
    entityManagerFactoryRef = "oracleEntityManagerFactory",
    transactionManagerRef = "oracleTransactionManager"
)
```

**5. Services inject the correct `EntityManager`** by `unitName`:
```java
@PersistenceContext(unitName = "oracle")
private EntityManager entityManager;
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- Oracle Database XE (running on `localhost:1521`)
- PostgreSQL (running on `localhost:5432`)

### 1. Clone the repository

```bash
git clone https://github.com/your-username/Multiple-Database-Connector.git
cd Multiple-Database-Connector/Day99114_MultiDataBa_EntityManager
```

### 2. Update credentials

Edit `src/main/resources/application.yml` and replace the database usernames and passwords with your own.

### 3. Build & Run

```bash
./mvnw spring-boot:run
```

Or build a JAR and run it:

```bash
./mvnw clean package
java -jar target/Day99114_MultiDataBa_EntityManager-0.0.1-SNAPSHOT.jar
```

The application starts on **port 8081**.

---

## 📡 API Endpoints

### 🏥 Doctors (stored in Oracle DB)

| Method | Endpoint | Description | Request Body |
|---|---|---|---|
| `POST` | `/doctors` | Add a new doctor | JSON (see below) |
| `GET` | `/doctors` | Retrieve all doctors | — |

**Sample Request — Add Doctor:**
```json
POST http://localhost:8081/doctors
Content-Type: application/json

{
  "docId": 1,
  "dname": "Dr. Alice Smith",
  "specialization": "Cardiology",
  "address": "123 Medical Lane, Mumbai"
}
```

**Sample Response:**
```json
{
  "docId": 1,
  "dname": "Dr. Alice Smith",
  "specialization": "Cardiology",
  "address": "123 Medical Lane, Mumbai"
}
```

---

### 🧑‍⚕️ Patients (stored in PostgreSQL DB)

| Method | Endpoint | Description | Request Body |
|---|---|---|---|
| `POST` | `/patients` | Add a new patient | JSON (see below) |
| `GET` | `/patients` | Retrieve all patients | — |

**Sample Request — Add Patient:**
```json
POST http://localhost:8081/patients
Content-Type: application/json

{
  "name": "Ravi Kumar",
  "age": 34,
  "gender": "Male"
}
```

**Sample Response:**
```json
{
  "patientId": 1,
  "name": "Ravi Kumar",
  "age": 34,
  "gender": "Male"
}
```

---

## 🗃️ Database Schema

Tables are auto-created by Hibernate (`ddl-auto: update`) — no manual schema setup required.

**`doctorr` table (Oracle)**

| Column | Type | Constraint |
|---|---|---|
| `doc_id` | `NUMBER` | Primary Key |
| `dname` | `VARCHAR2` | — |
| `specialization` | `VARCHAR2` | — |
| `address` | `VARCHAR2` | — |

**`patients` table (PostgreSQL)**

| Column | Type | Constraint |
|---|---|---|
| `patient_id` | `SERIAL` | Primary Key (auto-generated) |
| `name` | `VARCHAR` | — |
| `age` | `INTEGER` | — |
| `gender` | `VARCHAR` | — |

---

## 🧠 Key Concepts Demonstrated

- **Multiple `DataSource` beans** in a single Spring Boot app without Spring Boot's auto-configuration conflict
- **`@Primary` annotation** to resolve ambiguity when Spring injects a default `DataSource` or `EntityManagerFactory`
- **`@PersistenceContext(unitName = "...")`** to inject database-specific `EntityManager` instances into services
- **Package-level isolation** — each database's entities and repositories live in their own packages, preventing cross-wiring
- **`@EnableJpaRepositories`** with explicit `entityManagerFactoryRef` and `transactionManagerRef` to bind repositories to the correct database
- **JPQL queries** via `EntityManager` as an alternative to Spring Data's derived query methods

---

## 🔧 Troubleshooting

**App fails to start with `No qualifying bean of type 'DataSource'`**
→ Make sure exactly one `DataSource` bean is marked `@Primary`. In this project, `postgresDataSource` is `@Primary`.

**`No EntityManager with actual transaction available for current thread`**
→ Ensure your service method is annotated with `@Transactional` when calling `entityManager.persist()`.

**Oracle tables not being created**
→ Confirm your Oracle user (`system`) has the `CREATE TABLE` privilege, and that `ddl-auto: update` is set.

**`Could not determine Hibernate dialect`**
→ Verify the JDBC URL format and that the correct JDBC driver (`ojdbc11` / `postgresql`) is on the classpath.

---

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests for improvements such as:

- Adding a third datasource (e.g., MySQL or MongoDB)
- Migrating to `spring.config.import` for external secrets
- Adding Swagger/OpenAPI documentation
- Writing integration tests with `@DataJpaTest`

---



> **Built with ❤️ using Spring Boot 3.x and Java 17**
