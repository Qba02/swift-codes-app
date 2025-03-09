# SWIFT CODES APPLICATION

## Overview
This project is a recruitment task that consists of a `Spring Boot` application 
connected to a `PostgreSQL` database. It stores information about banks (imported from a CSV file) 
in a relational database and provides several RESTful endpoints for interacting with the data.


#### What is SWIFT code?

A `SWIFT` code, also known as a `Bank Identifier Code (BIC)`, is a unique identifier of a bank's
branch or headquarter. It ensures that international wire transfers are directed to the correct
bank and branch, acting as a bank's unique address within the global financial network. 
Codes ending with `“XXX”` represent a bank's headquarters, otherwise
branch. Branch codes are associated with a headquarters if their first 8 characters
match

### Technologies

![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=fff)
![Postgres](https://img.shields.io/badge/Postgres-%23316192.svg?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=fff)

## Quick Start
This project is a **Spring Boot** application (with Maven) that connects to a **PostgreSQL** database, 
with **Docker** support to containerize both the application and the database. 
Below are the instructions on how to build and run the application.

---
### Project Setup (`DOCKER`)

#### Prerequisites

Before you begin, ensure you have the following tools installed on your machine:

- [Docker](https://www.docker.com/get-started) (to build and run containers)
- [Docker Compose](https://docs.docker.com/compose/install/) (to manage multi-container Docker applications)

**1. Clone the repository**

```
git clone https://github.com/Qba02/swift-codes-app.git
```

**2. Build and start the containers using the `docker-compose` command**
```
docker-compose up --build
```

**3. Test application by sending  request (for instance):**
```
http://localhost:8080/v1/swift-codes/country/PL
```

**4. Stop the application
(this will stop and remove the containers)** 
```
docker-compose down
```

---
### Project Setup (`LOCAL ENVIRONMENT`)

#### Prerequisites

Before you begin, ensure you have the following tools installed on your machine:
- [Maven](https://maven.apache.org/install.html) (to build the Spring Boot application)
- [JDK 21 or higher](https://www.oracle.com/java/technologies/downloads/) (for building and running the Spring Boot application locally)
- [PostgreSQL 17](https://www.postgresql.org/download/) (for storing data)

**1. Clone the repository**

```
git clone https://github.com/Qba02/swift-codes-app.git
```

**2. Build and run the application locally using `Maven`**
```
mvn spring-boot:run
```

**3. Test application by sending  request (for instance):**
```
http://localhost:8080/v1/swift-codes/country/PL
```

#### Final Notes
- Make sure your PostgreSQL database is up and running when testing the application locally.
- If you encounter any issues while running the application, check the logs for troubleshooting.


## Details

**Currently, this application provides following REST endpoints:**

**1.** Retrieve details of a single SWIFT code whether for a headquarters or
  branches.

**Request**
```
GET: /v1/swift-codes/{swift-code}
```
**Response (`headquarter`):**

```json lines
{
  "bank": {
    "address": string,
    "bankName": string,
    "countryISO2": string,
    "countryName": string,
    "isHeadquarter": bool,
    "swiftCode": string
  },
  "branches": [
    {
      "address": string,
      "bankName": string,
      "countryISO2": string,
      "isHeadquarter": bool,
      "swiftCode": string
    },
    ...
  ]
}
```

**Response (`branch`):**

```json lines
{
    "address": string,
    "bankName": string,
    "countryISO2": string,
    "countryName": string,
    "isHeadquarter": bool,
    "swiftCode": string
}

```

---
**2.** Return all SWIFT codes with details for a specific country (both
  headquarters and branches).

**Request**
```
GET: /v1/swift-codes/country/{countryISO2code}
```

**Response**

```json lines
{
    "countryISO2": string,
    "countryName": string,
    "swiftCodes": [
        {
            "address": string,
            "bankName": string,
            "countryISO2": string,
            "isHeadquarter": bool,
            "swiftCode": string
        },
        {
            "address": string,
            "bankName": string,
            "countryISO2": string,
            "isHeadquarter": bool,
            "swiftCode": string
        },
      ...
    ]
}
```


---
**3.** Adds new SWIFT code entries to the database for a specific country.

**Request**
```
POST: /v1/swift-codes
```

**Response**

```json lines
{
    "message": string
}
```

---
**4.** Deletes swift-code data if swiftCode matches the one in the database

**Request**
```
DELETE: /v1/swift-codes/{swift-code}
```

**Response**
```json lines
{
    "message": string
}
```