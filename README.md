# Minimal E-Commerce Backend API

Author: Prithviraj Pillai

Course: In-Class Assignment — Build a Minimal E-Commerce Backend API (Spring Boot)

Overview
--------
This repository contains a minimal e-commerce backend implemented with Spring Boot. The system supports:

- Product management (create, list)
- Cart management (add, view, clear)
- Creating orders from cart
- Payment initiation and webhook callback handling (Razorpay or mock service)
- Order status updates after payment

The implementation follows the assignment specification provided by the instructor and is intended for teaching core backend concepts: REST APIs, data modeling, repository/service/controller layering, webhook handling, and simple payment integration.

Quick Links
-----------
- App entry: `com.example.ecommerce.EcommerceApplication`
- Webhook endpoint: `POST /api/webhooks/payment`
- Default app port: `8080` (configurable in `application.yaml`)

Getting started
---------------
Requirements
- Java 11+ (or the version configured in `pom.xml`)
- Maven (wrapper included)
- (Optional) MongoDB if you use a real datastore (the sample project may be pre-configured for MongoDB)

Run locally (Windows PowerShell)

```powershell
cd "B:\Trimester-6\Spring-boot\Minimal-E-Commerce-Backend-API\ecommerce"
# Build (skip tests for a quick smoke build)
.\mvnw -DskipTests package
# Run
.\mvnw spring-boot:run
```

If you prefer an IDE, import this folder as an existing Maven project.

Project structure
-----------------
Top-level package: `com.example.ecommerce`

Recommended folder structure (as present in the project):

com.example.ecommerce
├── controller
│   ├── ProductController.java
│   ├── CartController.java
│   ├── OrderController.java
│   └── PaymentController.java
├── service
│   ├── ProductService.java
│   ├── CartService.java
│   ├── OrderService.java
│   └── PaymentService.java
├── repository
│   ├── ProductRepository.java
│   ├── CartRepository.java
│   ├── OrderRepository.java
│   └── PaymentRepository.java
├── model
│   ├── User.java
│   ├── Product.java
│   ├── CartItem.java
│   ├── Order.java
│   ├── OrderItem.java
│   └── Payment.java
├── dto
│   ├── AddToCartRequest.java
│   ├── CreateOrderRequest.java
│   ├── PaymentRequest.java
│   └── PaymentWebhookRequest.java
├── webhook
│   └── PaymentWebhookController.java
├── client
│   └── PaymentServiceClient.java  // if mock service used
├── config
│   └── RestTemplateConfig.java
└── EcommerceApplication.java

File-level documentation
-----------------------
Note: The list below maps to source files in `src/main/java/com/example/ecommerce` and describes each file's role.

- EcommerceApplication.java
  - The Spring Boot application entry point. Bootstraps the app and component scanning.

- config/RestTemplateConfig.java
  - Provides a configured `RestTemplate` bean used for service-to-service calls (e.g., calling a mock payment service).

Controllers
- controller/ProductController.java
  - Endpoints: `POST /api/products`, `GET /api/products`
  - Handles creation and listing of products.

- controller/CartController.java
  - Endpoints: `POST /api/cart/add`, `GET /api/cart/{userId}`, `DELETE /api/cart/{userId}/clear`
  - Add items to cart, fetch a user's cart, and clear the cart.

- controller/OrderController.java
  - Endpoints: `POST /api/orders`, `GET /api/orders/{orderId}`
  - Create orders from cart contents and fetch order details.

- controller/PaymentController.java
  - Endpoints: `POST /api/payments/create`
  - Initiates payments (either calls Razorpay API or a mock service). Returns a payment record with status `PENDING`.

- webhook/PaymentWebhookController.java
  - Endpoint: `POST /api/webhooks/payment`
  - Receives asynchronous webhooks from the payment gateway (Razorpay or mock). Validates payload and updates `Payment` and `Order` status accordingly.

Services
- service/ProductService.java
  - Business logic for product creation and retrieval.

- service/CartService.java
  - Business logic to add items to cart, list cart items for a user, and clear a user's cart. Handles validation like product existence and stock checks.

- service/OrderService.java
  - Converts cart items into an order, calculates total amount, creates order items, updates product stock, and clears the cart. Manages order lifecycle (CREATED → PAID → FAILED/CANCELLED).

- service/PaymentService.java
  - Creates a `Payment` record, calls the payment gateway (or mock), and stores gateway identifiers (e.g., `razorpayOrderId` or mock `paymentId`).

Repositories
- repository/ProductRepository.java
  - Spring Data repository for Product entity.

- repository/CartRepository.java
  - Spring Data repository for CartItem entity.

- repository/OrderRepository.java
  - Spring Data repository for Order and OrderItem entities.

- repository/PaymentRepository.java
  - Spring Data repository for Payment entity.

Models / Entities
- model/User.java
  - Fields: `id`, `username`, `email`, `role`.

- model/Product.java
  - Fields: `id`, `name`, `description`, `price`, `stock`.

- model/CartItem.java
  - Fields: `id`, `userId`, `productId`, `quantity`.

- model/Order.java
  - Fields: `id`, `userId`, `totalAmount`, `status`, `createdAt`.

- model/OrderItem.java
  - Fields: `id`, `orderId`, `productId`, `quantity`, `price`.

- model/Payment.java
  - Fields: `id`, `orderId`, `amount`, `status`, `paymentId`, `createdAt`.

DTOs
- dto/AddToCartRequest.java
  - Request payload to add items to cart: `{ userId, productId, quantity }`.

- dto/CreateOrderRequest.java
  - Request payload to create an order from cart: `{ userId }`.

- dto/PaymentRequest.java
  - Request payload to create a payment: `{ orderId, amount }`.

- dto/PaymentWebhookRequest.java
  - Payload mapping for webhook data received from the payment gateway.

Database schema / ER Diagram
----------------------------
Entities and relationships (textual schema):

USER (1) --- (N) CART_ITEM
USER (1) --- (N) ORDER
PRODUCT (1) --- (N) CART_ITEM
PRODUCT (1) --- (N) ORDER_ITEM
ORDER (1) --- (N) ORDER_ITEM
ORDER (1) --- (1) PAYMENT

Entity fields (summary):
- USER: id (PK), username, email, role
- PRODUCT: id (PK), name, description, price, stock
- CART_ITEM: id (PK), userId (FK), productId (FK), quantity
- ORDER: id (PK), userId (FK), totalAmount, status, createdAt
- ORDER_ITEM: id (PK), orderId (FK), productId (FK), quantity, price
- PAYMENT: id (PK), orderId (FK), amount, status, paymentId, createdAt

(You can convert the above into a visual ER diagram using tools like draw.io or PlantUML.)

APIs (Mandatory)
----------------
1) Product APIs
- POST /api/products
  - Request body:
  ```json
  {
    "name": "Laptop",
    "description": "Gaming Laptop",
    "price": 50000.0,
    "stock": 10
  }
  ```
  - Response: Created product object with `id`.

- GET /api/products
  - Response: Array of products

2) Cart APIs
- POST /api/cart/add
  - Request body:
  ```json
  {
    "userId": "user123",
    "productId": "prod123",
    "quantity": 2
  }
  ```
  - Response: Cart item object.

- GET /api/cart/{userId}
  - Response: List of cart items for the user. Each item may include product details.

- DELETE /api/cart/{userId}/clear
  - Response: { "message": "Cart cleared successfully" }

3) Order APIs
- POST /api/orders
  - Request body:
  ```json
  { "userId": "user123" }
  ```
  - Response: Created order with status `CREATED` and list of order items.

- GET /api/orders/{orderId}
  - Response: Order details including payment info (if available).

4) Payment APIs
Option A: Razorpay (optional)
- POST /api/payments/create
  - Request body:
  ```json
  { "orderId": "order123", "amount": 100000.0 }
  ```
  - Response: Payment record including gateway-specific identifiers (e.g., `razorpayOrderId`) and status `PENDING`.

- POST /api/webhooks/payment
  - Razorpay will send a webhook like:
  ```json
  {
    "event": "payment.captured",
    "payload": {
      "payment": {
        "id": "pay_razorpay123",
        "order_id": "order_xyz",
        "status": "captured"
      }
    }
  }
  ```
  - The webhook endpoint should validate the event and update Payment and Order status accordingly.

Option B: Mock Payment Service (default / recommended for the assignment)
- POST /api/payments/create
  - Request body same as above.
  - Response: Payment record with `status` = `PENDING` and an internal `paymentId` (e.g., `pay_mock123`).
  - The mock service should call the webhook endpoint automatically after ~3 seconds to simulate a successful payment. The webhook handler will update the order status to `PAID`.

Order flow (summary)
--------------------
1. Create product(s)
2. Add items to cart
3. View cart
4. Create order from cart (`status`: CREATED)
5. Initiate payment (`status`: PENDING)
6. Payment gateway calls webhook after processing
7. Webhook updates payment and order (`status`: PAID)
8. Client checks order status

Testing with Postman
--------------------
- Create products (POST /api/products) — save product IDs as variables.
- Add to cart (POST /api/cart/add) — use userId variable.
- Get cart (GET /api/cart/{userId})
- Create order (POST /api/orders)
- Create payment (POST /api/payments/create)
- Wait for webhook (mock) or complete payment in Razorpay (if integrated)
- Get order status (GET /api/orders/{orderId})

Add Postman Variables (recommended):
- userId
- productId
- orderId
- paymentId

Sample data (for quick testing)
```json
[
  { "name": "Laptop", "description": "Gaming Laptop", "price": 50000.0, "stock": 10 },
  { "name": "Mouse", "description": "Wireless Mouse", "price": 1000.0, "stock": 50 },
  { "name": "Keyboard", "description": "Mechanical Keyboard", "price": 3000.0, "stock": 30 }
]
```

Implementation checklist (mapped to files)
-----------------------------------------
- [ ] ProductController + ProductService + ProductRepository + Product model
- [ ] CartController + CartService + CartRepository + CartItem model
- [ ] OrderController + OrderService + OrderRepository + Order, OrderItem models
- [ ] PaymentController + PaymentService + PaymentRepository + Payment model
- [ ] webhook/PaymentWebhookController for handling payment callbacks
- [ ] DTOs for request/response mapping
- [ ] RestTemplateConfig for calling mock payment service

Grading checklist
-----------------
(Keep this handy when submitting the assignment)
- Product APIs implemented and tested — 15 points
- Cart APIs implemented and tested — 20 points
- Order APIs implemented and tested — 25 points
- Payment & webhook handling — 30 points
- Order status updates after payment — 10 points
- Code quality, structure, comments — 10 points
- Postman collection — 10 points
- Razorpay bonus — +10 points (optional)

Extra (optional) features / bonus
---------------------------------
- GET /api/orders/user/{userId} — list order history for a user
- POST /api/orders/{orderId}/cancel — cancel order if not paid (restore stock)
- GET /api/products/search?q=... — search products by name or description

Troubleshooting
---------------
- If the app fails to connect to MongoDB, verify `spring.data.mongodb.uri` in `application.yaml` or switch to an embedded/in-memory DB for testing.
- For debugging webhooks locally you can use a tool like ngrok to expose your local port and configure the payment service to call the public URL.

References
----------
- Spring Boot Reference
- Spring Data MongoDB Guide
- RestTemplate usage
- Razorpay integration docs (if using Razorpay)

Contact
-------
For questions about the assignment and grading, contact the instructor or the course TA. For code-specific queries, you can inspect the controller and service implementations in `src/main/java/com/example/ecommerce`.

---

Requirements coverage
---------------------
- Products: documented and included (ProductController/ProductService)
- Cart: documented (CartController/CartService)
- Orders: documented (OrderController/OrderService)
- Payments/webhooks: documented (PaymentController/PaymentService, webhook/PaymentWebhookController)
- Postman: recommended testing steps and variables included



