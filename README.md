# ENSF-480-Flight-Reservation-Application
---
## Project Background

In this project, we were tasked with developing a flight reservation application using the skills we acquired throughout the semester.

At the beginning of the project, our group was torn between two possible design approaches:

- Creating a highly realistic reservation system that would support multiple airlines  
- Building a moderately realistic system intended for a startup airline that manages only its own flights  

We carefully evaluated both options. After discussion, we recognized that neither administrators nor agents would realistically have the ability to manage third-party airlines or add new airlines into the system. Since this functionality would not align with realistic role permissions, we decided to design a reservation system focused on a single airline.

Our system is modeled after the functionality of real-world airline platforms. To guide the look and behavior of our application, we drew inspiration from websites such as **KLM** and **Qatar Airways**.

---

## System Overview

The system begins with a **login page**, where users are prompted to enter their credentials.

- If a user is not yet registered, they are given the option to create an account and become a customer.
- Registered users can simply enter their credentials and proceed.

After a successful login, the interface displayed depends on the user’s role, which is stored in the database. The system queries the database to retrieve the user’s role and then loads one of three user interfaces:

- **Customer UI**
- **Agent UI**
- **Admin UI**

Each interface provides different functionalities based on the permissions associated with that role. These role-based interactions and permissions are illustrated in our use case diagram.

---

## System Requirements

To run the system, the following requirements must be met:

- **JDK 25 or later** must be installed.
- The image files included in the zip file must be placed in the correct location (in the same directory as the `src` folder).
- **MySQL** and **MySQL Workbench** must be installed.
- The attached SQL script must be executed in MySQL Workbench to generate the required database for the system.
  - Data may be updated as desired.
  - Column names must remain unchanged to ensure proper system functionality.
- `mysql-connector-j-9.5.0.jar` must be added to the Java workspace and included in the build path.

---

## Design Documentation

Design documents are included for a more in-depth look at the application’s architecture, structure, and functionality.

---

## Developed By

- Ebitimi Dambo  
- Aadil Bashir  
- Arol Nokam Wafo  
- Erin Kim  
