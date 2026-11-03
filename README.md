# Backend Bus Reservation System

This repository contains three Spring Boot services for user accounts, bus
inventory and reservations, and Stripe-backed payments. Each service is a
separate Maven application with its own database configuration.

## Services and startup

| Service | Directory | Default port | Java version |
|---|---|---:|---:|
| User service | `Userservice/` | 8081 | 17 |
| Bus service | `Busserivce/` | 8082 | 17 |
| Payment service | `PaymentService/` | 8083 | 21 |

Set the database connection properties referenced by each service's
`src/main/resources/application.properties` before starting it. The bus service
also needs `payment.service.url`; the payment service needs `BUS_SERVICE_URL`,
`stripe.secret.key`, and `stripe.webhook.secret`. Supply secrets through the
runtime environment or a secret manager; do not commit them.

From each service directory, run:

```bash
./mvnw spring-boot:run
```

Build a service with `./mvnw test`. The current tests in all three projects only
check that the Spring application context loads; they do not exercise API
behavior.

## API conventions

The API tables below describe routes present in the controllers. JSON field
names follow the Java DTOs, including existing underscore/capitalization
conventions such as `phone_no`, `dept_Date`, and `TripId`. No API version prefix
is configured. Unless noted otherwise, request and response bodies are JSON.
The source does not define a consistent API-wide error schema or status-code
contract.

### User service (`http://localhost:8081`)

Base path: `/user`

| Method and path | Purpose and behavior | Request body / response |
|---|---|---|
| `GET /user/exist/{email}` | Checks whether the email is a user ID. | Returns a boolean. |
| `POST /user/validate` | Checks the supplied password against the stored password hash and also compares name and phone number. | `{"email":"...","password":"...","name":"...","phone_no":1234567890}`. Returns public user details on success. |
| `POST /user` | Creates a user after rejecting an already registered email. The password is BCrypt-encoded before persistence. | `{"email":"...","password":"...","name":"...","phone_no":1234567890}`. Returns user details. |
| `PUT /user` | Requires the current password, then changes the name, phone number, and password. | `{"email":"...","currPassword":"...","changePassword":"...","name":"...","phone_no":1234567890}`. Returns user details. |
| `GET /user/{email}` | Looks up a user by email. The response omits the password. | Returns email, name, and phone number. |
| `DELETE /user/` | Checks the supplied password and permanently deletes that user. | `{"email":"...","password":"..."}`. Returns an empty success response. |

The user service's `UserService` methods implement those operations:
`checkUser` tests existence; `validUser` compares credentials and profile data;
`createUser` hashes and saves credentials; `updateUser` checks the current
password before changing profile data; `search` returns public profile
information; and `deleteUser` verifies the password before deleting the row.
The global exception handler maps the service's known user, name, phone, and
password exceptions to HTTP responses.

### Bus service (`http://localhost:8082`)

#### Buses — base path `/api/buses`

| Method and path | Purpose and behavior | Request body / response |
|---|---|---|
| `POST /api/buses` | Registers a bus. | `{"name":"...","total_seats":40,"bus_Type":"...","adminEmailId":"...","status":"ACTIVATE"}`. Returns the created bus. |
| `GET /api/buses/{bus_Id}` | Gets one bus by ID. | Returns bus ID, name, seat count, type, admin email, and status. |
| `GET /api/buses?page={page}&size={size}` | Returns a paginated list of buses. Both query parameters are required. | Returns a Spring `Page` of buses. |
| `PUT /api/buses/{busId}` | Updates a bus using the supplied update DTO. | Same fields as bus creation; returns the updated bus. |
| `DELETE /api/buses/{bus_id}` | Marks the bus `DEACTIVATE`; it does not physically delete the row. | Empty success response on the expected status. |

#### Routes — base path `/api/routes`

| Method and path | Purpose and behavior | Request body / response |
|---|---|---|
| `POST /api/routes` | Creates a route. | `{"startplace":"...","endplace":"...","adminEmailId":"...","routeStatus":"ACTIVATE"}`. Returns the route. |
| `GET /api/routes/{routeId}` | Gets a route by ID. | Returns route ID, start/end places, admin email, and status. |
| `GET /api/routes?page={page}&size={size}` | Returns a paginated list of routes. Both query parameters are required. | Returns a Spring `Page` of routes. |
| `PUT /api/routes/{routeId}` | Updates the route's start and end places. | Accepts the route update DTO (`startplace`, `endplace`, `adminEmailId`, `routeStatus`); returns the route. |
| `DELETE /api/routes/{routeId}` | Marks the route `DISACTIVATE`; it does not physically delete the row. | Empty success response. |

#### Seats

| Method and path | Purpose and behavior | Request body / response |
|---|---|---|
| `POST /api/buses/{busId}/seats` | Adds one seat to a bus. | `{"seat_type":"...","fare":500}`. Returns the created seat. |
| `GET /api/buses/{busId}/seats?page={page}&size={size}` | Lists the bus's seats as a paginated page. It does not filter to available seats. | Both query parameters are required; returns seat ID, type, fare, and bus ID. |
| `PUT /api/seats/{seatId}` | Updates a seat using `seat_type`, `fare`, and `busId`. | Returns the updated seat. |
| `DELETE /api/seats/{seatId}` | Sets the seat status to `BLOCK`. Despite the HTTP verb, this blocks rather than deletes the seat. | No response body is declared. |

#### Trips — base path `/api/trips`

| Method and path | Purpose and behavior | Request body / response |
|---|---|---|
| `POST /api/trips` | Schedules a trip and associates an existing bus and route. | `{"start_time":"08:00:00","end_time":"12:00:00","dept_Date":"2026-11-04","total_time":4,"tripStatus":"SCHEDULED","busId":"...","routeId":"...","adminId":"..."}`. Returns the trip. |
| `GET /api/trips/{tripId}` | Gets a trip by ID. | Returns trip schedule/status and admin ID. |
| `GET /api/trips?page={page}&size={size}` | Returns a paginated list of trips. Both query parameters are required. | Returns a Spring `Page`; the controller currently responds with HTTP 302 (`FOUND`). |
| `PUT /api/trips/{tripId}` | Updates a trip using the trip update DTO. | Accepts schedule, status, admin, bus, and route fields; returns the trip. |
| `DELETE /api/trips/{tripId}` | Marks the trip `CANCELLED`; it does not physically delete it. | Empty success response. |

#### Bookings — base path `/api/bookings`

| Method and path | Purpose and behavior | Request body / response |
|---|---|---|
| `POST /api/bookings` | Checks that the requested seat is available, then attempts payment through the payment service. The booking is persisted only if the response status is `SUCCESS`. | `{"userId":"...","seatId":"...","fare":500,"routeId":"...","TripId":"..."}`. On success, returns booking ID, amount, user ID, seat ID, route ID, and trip ID. |
| `GET /api/bookings/{id}` | Gets a booking by ID. | Returns the booking response fields. |
| `GET /api/bookings/user/{user_id}?page={page}&size={size}` | Lists a user's bookings. Page defaults to `0`, size to `10`. | Returns a Spring `Page` of booking responses. |
| `POST /api/bookings/{id}/cancel` | Requests a payment refund/cancellation; when the payment service reports an accepted refund/cancel state, marks the booking cancelled and makes its seat available. | Empty success response, or a bad-request response when the resulting booking status is not cancelled. |
| `GET /api/bookings/{bookingId}/payment-details` | Internal endpoint used by the payment service to fetch booking details. | Returns the same booking response as the booking lookup endpoint. |

The bus service's principal service functions are:

* `BusSerivce`: `createBus`, `getBusById`, `getAllBuses`, `getAdminBuses`,
  `updateBus`, `deactivateBus`, and `busExists` handle bus persistence,
  pagination, status changes, and an internal existence check.
* `RouteService`: `createRoute`, `getRouteById`, `getAllRoutes`,
  `searchRoutes`, `updateRoute`, `deleteRoute`, and `routeExists` handle route
  CRUD, pagination, route-place search, and an internal existence check.
* `SeatService`: `createSeats`, `getAvailableSeats`, `getTripSeats`,
  `getSeatById`, `blockSeat`, `unblockSeat`, `updateSeat`, and
  `isSeatAvailable` handle bus-seat association, listing, availability, and
  status changes. Only create/list/update/block are currently exposed by HTTP.
* `TripService`: `createTrip`, `getTripById`, `getAvailableTrips`,
  `searchTrips`, `updateTrip`, `cancelTrip`, and `getAdminTrips` handle trip
  scheduling, lookup, search, update, cancellation, and admin filtering. Only
  create/list/get/update/cancel are exposed by HTTP.
* `BookingService`: `createBooking`, `getBookingById`, `getUserBookings`,
  `cancelBooking`, `getBookingStatus`, `confirmBooking`, and `bookingExists`
  handle reservations and payment coordination. Booking-status, confirmation,
  and existence helpers are not exposed as dedicated HTTP endpoints.
* `PaymentClient` constructs the configured payment-service `RestClient`.
  DTO/entity mappers convert request, response, and persistence objects;
  Spring Data repositories perform database reads and writes.

### Payment service (`http://localhost:8083`)

Base path: `/api/payments`

| Method and path | Purpose and behavior | Request body / response |
|---|---|---|
| `POST /api/payments` | Fetches booking amount/currency from the bus service, creates a Stripe Checkout Session, and stores a `PENDING` payment record. | `{"bookingId":"..."}`. Returns mapped payment details; the checkout URL is not currently populated in the response. |
| `GET /api/payments/{paymentId}` | Gets a payment by payment ID. | Returns payment ID, booking ID, amount, status, currency, and timestamps where available. |
| `GET /api/payments/booking/{bookingId}` | Gets a payment by booking ID. | Returns the payment details. |
| `POST /api/payments/{paymentId}/cancel` | Initiates a Stripe refund for a payment in `SUCCESS`, then updates its status based on the Stripe response. | Returns the updated payment details. |
| `POST /api/payments/webhook` | Verifies the `Stripe-Signature` header against the configured webhook secret and processes supported Stripe events. | Raw Stripe event body plus the required `Stripe-Signature` header. Returns a processing message or an error response. |

`PaymentService.createPayment` creates the Checkout Session; `getPaymentById`
and `getPaymentBybookingId` look up stored payments; `cancelRefundPayment`
starts an eligible refund; and `handleStripeEvent` updates payment/refund state
from supported Stripe webhook events. `BusServiceClient.getPaymentPayment`
calls the bus service's booking payment-details endpoint. The webhook handles
Checkout Session completion/asynchronous success or failure and refund
created/updated/failed events; other event types are ignored. Its internal
helpers `createCheckoutSession`, `getStripeEventObject`, and
`handleRefundEvent` build Stripe sessions, deserialize webhook data, and
synchronize refund status.

Payment statuses currently defined are `PENDING`, `SUCCESS`, `FAILED`,
`CANCELLED`, `CREATED`, `REFUNDED`, `REFUND_FAILED`, and `REFUND_PENDING`.
Booking statuses are `CONFIRMED`, `BLOCKED`, `WAITLISTED`, `REJECTED`,
`COMPLETED`, and `CANCELLED`. Seat statuses are `AVAILABLE`, `CONFIRMED`,
`REJECTED`, and `BLOCK`; bus and route statuses support activation/deactivation;
trip statuses are `SCHEDULED`, `BOARDING`, `IN_PROGRESS`, `COMPLETED`, and
`CANCELLED`.

## Current gaps and limitations

The following items are not implemented or are incomplete in the current
source; the list is based on the code in this repository, not a product
roadmap.

1. **No authentication or role-based authorization.** The bus and user services
   permit all requests, and the payment service permits all `/api/payments/**`
   requests. Password verification exists for account operations, but there is
   no login/token/session flow protecting APIs or admin operations.
2. **Booking/payment flow is not fully integrated.** Booking creation calls
   payment before saving the booking and expects an immediate `SUCCESS`, while
   payment creation creates a Stripe Checkout Session and returns `PENDING`.
   The payment service also fetches booking details before the booking has been
   persisted. In addition, the payment service expects booking details named
   `bookingId` and `currency`, but the bus service's booking response uses
   `book_id` and does not provide currency. These contracts need alignment for
   the end-to-end flow to work.
3. **Successful payment does not confirm the booking asynchronously.** The
   payment webhook updates payment status, but the code contains a TODO rather
   than notifying the bus service to confirm a booking after Stripe confirms
   payment.
4. **Checkout redirect details are not returned/configurable.** Checkout
   success and cancellation URLs are hard-coded to `localhost:3000`, and the
   payment response DTO's `checkoutUrl` is not populated, so a client cannot
   rely on the create-payment response to obtain the Stripe Checkout URL.
5. **Several service functions have no HTTP endpoint.** Route searching,
   available-seat listing, seat lookup/unblock/availability checks, trip
   searching/admin filtering, bus admin filtering/existence checks, and
   booking status/confirmation/existence methods are service-level only.
6. **No user-to-booking integration.** User creation, validation, and deletion
   are not connected to the bus/booking service, which accepts `userId` without
   verifying the corresponding user.
7. **Input validation and API contract consistency are limited.** A payment
   request field has a validation annotation, but the controllers do not
   consistently invoke bean validation. Most other request DTOs have no
   validation constraints; pagination parameters are inconsistent; and error
   responses are not standardized.
8. **Operational/API documentation and tests are sparse.** There is no
   OpenAPI/Swagger contract, and the existing tests only load application
   contexts rather than testing controller, persistence, payment, or booking
   behavior.
9. **Concurrency and lifecycle rules need coverage.** Seat availability is
   checked before booking without an atomic reservation/locking mechanism, so
   concurrent requests may attempt to reserve the same seat. Automatic seat
   release for failed/expired payments and trip-cancellation handling for
   existing bookings are not implemented in the exposed workflows.
