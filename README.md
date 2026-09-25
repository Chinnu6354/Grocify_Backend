\# 🛒 Grocify Backend



Grocify Backend is a REST API built with \*\*Java and Spring Boot\*\* for the Grocify grocery shopping application.



It provides APIs for authentication, products, cart, wishlist, orders, contact messages, OTP verification, image uploads, and user management.



\---



\## 🚀 Features



\### 🔐 Authentication \& Security



\- User registration

\- User login

\- JWT authentication

\- Spring Security

\- Password encryption using BCrypt

\- OTP verification

\- Forgot password

\- Password reset

\- Protected API endpoints

\- Role-based authorization for Admin APIs



\### 🛍️ Product Management



\- Create products

\- Get products

\- Update products

\- Delete products

\- Product image upload

\- Cloudinary image storage

\- Admin-only product management



\### 🛒 Cart



\- Add products to cart

\- Update cart quantity

\- Remove cart items

\- Get user's cart



\### ❤️ Wishlist



\- Add products to wishlist

\- Remove products from wishlist

\- Get user's wishlist



\### 📦 Orders



\- Create orders

\- Get user orders

\- Get order details

\- Admin order management

\- Update order status



\### 📧 Email \& OTP



\- OTP generation

\- OTP verification

\- Password reset through OTP

\- Gmail SMTP integration



\### 📩 Contact



\- Submit contact messages

\- Store contact messages in MySQL

\- Admin access to contact messages



\---


# 🏗️ Architecture

```text
React.js Frontend
       │
       │ REST API / JSON
       ▼
Spring Boot Backend
       │
       ├── Controller Layer
       │
       ├── Service Layer
       │
       ├── Repository Layer
       │
       ▼
     MySQL
       
       ├── Cloudinary
       │     └── Product Images
       │
       └── Gmail SMTP
             └── OTP / Email





💻 Technologies

Backend:
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- REST API
- JWT
- Maven
- Database
- MySQL
- External Services
- Cloudinary
- Gmail SMTP
- Tools
- IntelliJ IDEA
- Postman
- Git
GitHub





📁 Project Structure

src/
└── main/
    ├── java/
    │   └── com/grocify/backend/
    │
    │       ├── config/
    │       │   ├── CloudinaryConfig.java
    │       │   └── SecurityConfig.java
    │       │
    │       ├── controller/
    │       │   ├── AuthController.java
    │       │   ├── CartController.java
    │       │   ├── ContactMessageController.java
    │       │   ├── OrderController.java
    │       │   ├── OtpController.java
    │       │   ├── ProductController.java
    │       │   └── WishlistController.java
    │       │
    │       ├── dto/
    │       │
    │       ├── entity/
    │       │
    │       ├── repository/
    │       │
    │       ├── security/
    │       │   └── JwtAuthenticationFilter.java
    │       │
    │       └── service/
    │
    └── resources/
        └── application.properties

.env.example
Dockerfile
pom.xml
mvnw
mvnw.cmd



🔒 Security
The backend uses:

- Spring Security
- JWT authentication
- BCrypt password hashing
-Role-based authorization
- CORS configuration
- Environment variables for credentials
- Protected REST endpoints


🧪 API Testing

- The APIs can be tested using Postman.
- The project includes Postman collections for testing the backend APIs.


Typical flow:

Signup
   ↓
OTP Verification
   ↓
Login
   ↓
JWT Token
   ↓
Authenticated API Requests
   ↓
Cart / Wishlist / Orders

Cart / Wishlist / Orders





🔗 Frontend



The backend is designed to work with the Grocify React frontend.



Frontend repository:



https://github.com/Chinnu6354/Grocify\_Clone



Backend repository:



https://github.com/Chinnu6354/Grocify\_Backend





👨‍💻 Author



Chinnu Pradhan



Full Stack Developer



Java • Spring Boot • React.js • MySQL • REST APIs 





\### One thing I'd change from the frontend README



For the backend README, \*\*don't claim deployment is completed yet\*\*. We have verified it locally, but we're still working on cloud deployment.



So the current status is:



\*\*Local:\*\* ✅ Complete  

\*\*GitHub:\*\* ✅ Complete  

\*\*Production deployment:\*\* ⏳ Next step



