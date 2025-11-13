# Warehouse Management System 📦

A **robust, distributed Warehouse Management System** built with **Java**, utilizing **gRPC** for high-performance communication and **JavaFX** for a responsive client interface. The system follows a clean **Layered Architecture** and enforces strict validation, authentication, and error handling.

---

## 🚀 Technology Stack

| Component            | Technology                                      |
|----------------------|-------------------------------------------------|
| **Language**         | Java 17+                                        |
| **Build Tool**       | Maven                                           |
| **Communication**    | gRPC (Protocol Buffers)                         |
| **GUI Framework**    | JavaFX                                          |
| **Data Storage**     | JSON (via Gson) - *Simulating a NoSQL approach* |
| **Authentication**   | JWT (JSON Web Tokens)                           |
| **Password Hashing** | BCrypt                                          |
| **Architecture**     | Layered (`Controller -> Service -> Repository -> DataSource`) |

---

## 📂 Project Structure

The project is organized to ensure **Separation of Concerns (SoC)** and easy maintainability.

```text
warehouse/
├── src/main/
│   ├── proto/                  # gRPC Definitions (Split by Domain)
│   │   ├── common/             # Common messages (Empty, Pagination, Status)
│   │   ├── auth/               # Authentication & Profile services
│   │   ├── user/               # User management services
│   │   ├── product/            # Product management services
│   │   └── warehouse/          # Inventory & Transaction logic
│   │
│   ├── java/
│   │   ├── client/             # === CLIENT SIDE (JavaFX) ===
│   │   │   ├── controller/     # JavaFX Controllers (Handle UI events)
│   │   │   ├── model/          # Client-side Models
│   │   │   ├── service/        # gRPC Client Stubs & Session Management
│   │   │   ├── util/           # Utilities (PDF Generator, Alerts)
│   │   │   └── ClientApp.java  # Client Entry Point
│   │   │
│   │   └── server/             # === SERVER SIDE (gRPC) ===
│   │       ├── container/      # Dependency Injection (ApplicationContainer)
│   │       ├── datasource/     # Data Access to JSON files (Low-level I/O)
│   │       ├── exception/      # Custom Business Exceptions (401, 403, 404...)
│   │       ├── grpc/           # gRPC Service Implementations (Controller Layer)
│   │       ├── interceptor/    # Global Error Handling & Auth Middleware
│   │       ├── mapper/         # Entity <-> Proto converters
│   │       ├── model/          # Server Domain Models
│   │       ├── repository/     # Data Access Layer (Repository Pattern)
│   │       ├── service/        # Business Logic & Transaction Management
│   │       ├── validator/      # Request Validation Logic
│   │       └── ServerApp.java  # Server Entry Point
│   │
│   └── resources/
│       ├── client/             # FXML Views, CSS Styles, Fonts
│       └── logback.xml         # Logging configuration
│
├── data/                       # JSON Storage (users.json, products.json...)
└── pom.xml                     # Maven Dependencies & Build config
```
# 🛠️ Setup & Installation

## Prerequisites
* **JDK 17** or higher installed.
* **Maven** installed and configured.

## Build Steps

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd warehouse
    ```

2.  **Compile and Generate gRPC Sources:**
    ```bash
    mvn clean install
    ```
    > This will automatically generate the gRPC code in `target/generated-sources/protobuf/`.

---

# 🏃‍♂️ How to Run

### 1. Start the Server
*The server must be running before the client can connect.*

* **Main Class:** `server.ServerApp`
* **Port:** Default is `9090`
* **Data Initialization:** The server will automatically create the `data/` folder and JSON files if they don't exist.

### 2. Start the Client
* **Main Class:** `client.ClientApp`
* **Login:** Use the default credentials if initialized (or check `data/users.json`).

---

# 🔐 Architecture Highlights

## Backend (Server)

* **Strict Layering:** The `grpc` package acts as a controller and never touches the file system. It delegates tasks to the service layer.
* **Global Exception Handling:**
    * A `GlobalExceptionHandlerInterceptor` captures all business exceptions (e.g., `ResourceNotFoundException`, `ValidationException`) and converts them into standard gRPC error status codes (`NOT_FOUND`, `INVALID_ARGUMENT`, etc.).

* **Interceptor Chain:**
    ```text
    GlobalExceptionHandler (Outer layer - catches errors)
        └── AuthInterceptor (Inner layer - validates JWT & Role permissions)
    ```

* **Request Validation:** All incoming requests are validated by `RequestValidator` before processing logic.

## Frontend (Client)

* **Robust Error Handling:**
    * The client uses `try-catch(StatusRuntimeException)` to gracefully handle server errors.
* **User Feedback:**
    * Displays specific error messages (e.g., "Product ID already exists", "Permission Denied") based on the gRPC status code returned by the server.
* **Session Management:**
    * JWT tokens are stored in memory to authenticate subsequent requests.
