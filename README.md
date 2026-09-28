<p align="center">
  <img src="src\main\resources\static\css\images\logo.png" alt="Tasmeem Hub Logo" width="180">
</p>

<h1 align="center">🎨 Tasmeem Hub</h1>

<p align="center">
  A web-based platform connecting clients with designers to collaborate,
  communicate, manage proposals, and deliver creative projects.
</p>
# 🎨 Tasmeem Hub

> **A web-based platform connecting clients with designers to collaborate, communicate, manage proposals, and deliver creative projects.**

Tasmeem Hub is a **capstone project developed as part of the Tuwaiq Academy Bootcamp – Web Development using Java**.

The project aims to simplify the process of connecting clients with designers and managing design projects from the initial request to final delivery.

The platform provides a complete workflow where clients can communicate with designers, browse designer catalogs, review proposals, make payments, follow project progress, and approve final drafts.

Designers can create their profiles and catalogs, communicate with clients, create proposals manually or with AI assistance, manage orders, and submit project drafts for client approval.

---

## 🎓 Bootcamp Context

**Program:** Tuwaiq Academy Bootcamp
**Track:** Web Development using Java
**Project Type:** Capstone Project
**Project:** Tasmeem Hub

This project was developed as the final capstone project of the **Tuwaiq Academy Web Development using Java Bootcamp**, applying the web development concepts and technologies learned throughout the program.

The project combines backend development, frontend development, database management, REST APIs, and AI-assisted functionality into one complete web application.

---

## ✨ Features

### 👤 Client

Clients can:

* Create and manage their account.
* Browse designer profiles.
* View designer biographies and catalogs.
* View images uploaded to designer catalogs.
* Communicate with designers through chat.
* Receive project proposals.
* Accept or reject proposals.
* View their orders and project status.
* View billing and payment information.
* Pay the required down payment.
* Open and inspect project drafts before making a decision.
* Accept or reject submitted drafts.
* Pay the remaining project amount after draft approval.

---

### 🎨 Designer

Designers can:

* Create and manage their designer profile.
* Add a professional biography.
* Create and manage catalogs.
* Upload images to their catalogs.
* Communicate with clients through chat.
* Create proposals manually.
* Generate proposal drafts using AI.
* Review and edit AI-generated proposals before sending them.
* Set the project price and deadline.
* Manage incoming orders.
* Track project progress.
* Submit project drafts to clients.
* Revise and resubmit rejected drafts.
* Complete projects after client approval.

---

## 🤖 AI-Assisted Proposal Generation

Tasmeem Hub includes an AI-assisted proposal feature that helps designers create proposals based on their conversation with the client.

The AI analyzes the conversation and generates a proposal containing relevant project information such as:

* Project details
* Price
* Deadline
* Requirements

The designer can then **review and edit the generated proposal before sending it to the client**.

This keeps the designer in control of the final proposal while reducing the time required to prepare one.

### Proposal Creation

Designers can choose between:

**Manual Proposal**

```text
Price
Deadline
Project Details
```

or

**AI-Assisted Proposal**

```text
Client ↔ Designer Conversation
          ↓
      AI Analysis
          ↓
   Generated Proposal
          ↓
 Designer Reviews/Edits
          ↓
    Proposal Sent
```

---

## 💬 Communication

Tasmeem Hub provides a communication system between clients and designers.

The chat allows both parties to discuss:

* Project requirements
* Design ideas
* Pricing
* Deadlines
* Revisions
* Project details

The conversation can also be used by the AI proposal feature to help generate a suitable proposal.

---

## 📋 Proposal Workflow

```text
Client & Designer Chat
        ↓
Proposal Created
        ↓
Client Reviews Proposal
        ↓
   ┌────┴────┐
   ↓         ↓
Accept     Reject
   ↓
Order Created
   ↓
Bill Created
```

Designers can create proposals manually or use the AI-assisted proposal generator.

---

## 💰 Payment & Billing

When a client accepts a proposal, the project enters the order and billing workflow.

The project price is divided into:

| Payment           | Percentage |
| ----------------- | ---------: |
| Down Payment      |        20% |
| Remaining Payment |        80% |
| **Total**         |   **100%** |

### Payment Workflow

```text
Proposal Accepted
       ↓
Order Created
       ↓
20% Down Payment
       ↓
Work In Progress
       ↓
Designer Sends Draft
       ↓
Client Inspects Draft
       ↓
Draft Accepted
       ↓
80% Remaining Payment
       ↓
Project Completed
```

---

## 📦 Order Management

Once a proposal is accepted, the project becomes an order that can be followed by both the client and designer.

The order uses the following statuses:

| Status                     | Description                                       |
| -------------------------- | ------------------------------------------------- |
| `Waiting for Down Payment` | Client needs to pay the initial 20%.              |
| `Work In Progress`         | Designer is working on the project.               |
| `Draft Sent`               | Designer has submitted a draft for client review. |
| `Done`                     | Project has been completed.                       |

---

## 📝 Project Drafts

Designers can submit project drafts to clients for review.

The client must **open and inspect the draft before accepting or rejecting it**.

### Draft Workflow

```text
Designer Creates Draft
        ↓
Draft Sent
        ↓
Client Opens Draft
        ↓
Client Inspects Draft
        ↓
    ┌───┴────┐
    ↓        ↓
 Accept    Reject
    ↓        ↓
Continue   Revision
    ↓        ↓
Payment    Resubmit
    ↓
Completed
```

If the client rejects a draft, the designer can revise the project and submit an updated version.

---

## 🖼️ Designer Catalogs

Designers can showcase their work through personal catalogs.

A designer can:

* Create catalogs.
* Add catalog information.
* Upload project/design images.
* Display their work to potential clients.

Clients can browse these catalogs when looking for a suitable designer.

---
## 📧 Email Notifications

Tasmeem Hub includes email notifications for important interactions between clients and designers.

Currently, the system provides two email notifications:

### 📩 Client Request Notification

When a client sends a project request to a designer, the designer receives an email notification informing them about the new request.

```text
Client Sends Request
        ↓
Designer Receives Email
        ↓
Designer Can Review the Request
```

### 📝 Project Draft Notification

When a designer submits a project draft, the client receives an email notification informing them that a draft is ready for review.

```text
Designer Sends Draft
        ↓
Client Receives Email
        ↓
Client Opens & Inspects Draft
        ↓
Accept / Reject
```

These notifications help ensure that both clients and designers are informed when an important action requires their attention.


## 🛠️ Technologies Used

### Backend

* **Java**
* **Spring Boot**
* **Spring Web**
* **Spring Data JPA**
* **Hibernate**
* **REST APIs**
* **Maven**

### Database

* **MySQL**

### Frontend

* **HTML5**
* **CSS3**
* **JavaScript**
* **Thymeleaf**

### AI

* AI-assisted proposal generation
* Conversation-based proposal generation

---

## 🎯 Project Objectives

The main objectives of Tasmeem Hub are to:

* Connect clients with suitable designers.
* Provide designers with a platform to showcase their work.
* Simplify communication between clients and designers.
* Provide both manual and AI-assisted proposal creation.
* Organize project orders and their progress.
* Provide a structured payment workflow.
* Allow clients to inspect project drafts before approval.
* Allow designers to revise rejected drafts.
* Provide a centralized platform for managing the complete design-project lifecycle.

---

## 👩‍💻 Capstone Project

This project was developed as part of the:

**Tuwaiq Academy — Web Development using Java Bootcamp**

The project demonstrates practical application of full-stack web development concepts, including:

* Object-oriented programming with Java
* Spring Boot application development
* RESTful API design
* Database integration
* Frontend development
* Client-server communication
* User roles and access
* CRUD operations
* Project and order management
* AI integration
* Software architecture and application design

---

## 🚀 Getting Started

### Prerequisites

Make sure you have the following installed:

* Java JDK
* Maven
* MySQL
* Git
* An IDE such as IntelliJ IDEA, Eclipse, or VS Code

### Clone the Repository

```bash
git clone https://github.com/RazanAlmadan/Capstone-2.git
cd Capstone-2
```

### Database Setup

Create a MySQL database and configure your credentials in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tasmeem_hub
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

### Run the Application

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Or run the Spring Boot application from your IDE.

---

## 📄 License

This project was developed as a **Tuwaiq Academy Web Development using Java Bootcamp capstone project** and is intended primarily for educational, demonstration, and portfolio purposes.
