# Java-shareit
Users can rent items, leave reviews on rented items, 
and create requests if the desired item is not yet available on the platform. 

## Table of Contents
- [Project Description](#project-description)
- [Features](#feature)
  - [Bookings](#bookings-bookings)
  - [Items](#items-items)
  - [Requests](#item-requests-requests)
  - [Users](#users-users)
- [Technical Stack](#technical-stack)

<a name="project-description"></a>
## Project Description

Java-ShareIt is a backend API server that provides item sharing and requesting among users. 

The system is implemented as microservices - one for request validation,
another for the rest of the business logic,
and the database is also connected as a separate microservice

## Feature


### Bookings `/bookings`

#### Get Booker's Bookings `?state={state}&from={from}&size={size}`
- **Method:** GET
- **Description:** Pageable search for all bookings made by current user
- **Request Parameters:**
    - User ID is passed as a header `X-Sharer-User-Id`
    - The `state` parameter indicates the booking statuses.
      It is optional and defaults to `ALL`.
      It can also take the following values:
      `CURRENT` for current bookings,
      `PAST` for completed bookings,
      `FUTURE` for upcoming bookings,
      `WAITING` for unconfirmed bookings,
      and `REJECTED` for rejected bookings
    - The `from` parameter specifies the index of the first displayed element from the list (default = 0)
    - The `size` parameter determines the number of elements to be displayed (default = 10)

#### Get Owner's Bookings `/owner?state={state}&from={from}&size={size}`
- **Method:** GET
- **Description:** Pageable search across all bookings for
  items belonging to the current user
- **Request Parameters:**
    - User ID is passed as a header `X-Sharer-User-Id`
    - The `state` parameter indicates the booking statuses.
      It is optional and defaults to `ALL`.
      It can also take the following values:
      `CURRENT` for current bookings,
      `PAST` for completed bookings,
      `FUTURE` for upcoming bookings,
      `WAITING` for unconfirmed bookings,
      and `REJECTED` for rejected bookings
    - The `from` parameter specifies the index of the first displayed element from the list (default = 0)
    - The `size` parameter determines the number of elements to be displayed (default = 10)

#### Get Booking `/{bookingId}`
- **Method:** GET
- **Description:** Retrieves the details of a specific booking.
  Access available for item owner or booker only
- **Request Parameters:**
    - Path Variable: `{bookingId}`
    - User ID is passed as a header `X-Sharer-User-Id`

#### Add New Booking
- **Method:** POST
- **Description:** Creating request for a booking
- **Request Parameters:**
    - User ID is passed as a header `X-Sharer-User-Id`
    - Request Body

#### Update Booking Status `/{bookingId}`
- **Method:** PATCH
- **Description:** Approving or rejecting booking request by item's owner
- **Request Parameters:**
    - User ID is passed as a header `X-Sharer-User-Id`
    - Path Variable `/{bookingId}`
    - Request Param `boolean isApproved`

### Items `/items`

#### Get Owner Items `?from={from}&size={size}`
- **Method:** GET
- **Description:** Pageable search for list of user's own items
- **Request Parameters:**
    - User ID is passed as a header `X-Sharer-User-Id`
    - The `from` parameter specifies the index of the first displayed element from the list (default = 0)
    - The `size` parameter determines the number of elements to be displayed (default = 10)

#### Search `/search?text={text}&from={from}&size={size}`
- **Method:** GET
- **Description:** Pageable search for list of all items
- **Request Parameters:**
    - User ID is passed as a header `X-Sharer-User-Id`
    - The `text` parameter is used to search for items that contain the specified text in their title or description
    - The `from` parameter specifies the index of the first displayed element from the list (default = 0)
    - The `size` parameter determines the number of elements to be displayed (default = 10)

#### Get Item `/{itemId}`
- **Method:** GET
- **Description:** Retrieves the details of a specific item.
- **Request Parameters:**
    - Path Variable: `{itemId}`
    - User ID is passed as a header `X-Sharer-User-Id`

#### Add New Item
- **Method:** POST
- **Description:** Adding new item by existing user
- **Request Parameters:**
    - User ID is passed as a header `X-Sharer-User-Id`
    - Request Body

#### Update Item `/{itemId}`
- **Method:** PATCH
- **Description:** Updating name, description or availability of
  existing item by its owner
- **Request Parameters:**
    - User ID is passed as a header `X-Sharer-User-Id`
    - Request Body
      `{ "name": "Item Updated Name",
      "description": "Item's new description",
      "available": "false"}`
    - Path Variable `/{itemId}`

#### Post Comment `/{itemId}/comment`
- **Method:** POST
- **Description:** Posting a comment from the user to the item they have booked
- **Request Parameters:**
    - User ID is passed as a header `X-Sharer-User-Id`
    - Path Variable: `{itemId}`
    - Request Body `{ "text": "Leave a comment here",
      "authorName": "Booker's name" }`

### Item Requests `/requests`

#### Search For All Item Requests `/all?from={from}&size={size}`
- **Method:** GET
- **Description:** Pageable search for list of all item requests
- **Request Parameters:**
    - User ID is passed as a header `X-Sharer-User-Id`
    - The `from` parameter specifies the index of the first displayed element from the list (default = 0)
    - The `size` parameter determines the number of elements to be displayed (default = 10)

#### Get Item Request `/{requestId}`
- **Method:** GET
- **Description:** Retrieves the details of a specific item request.
- **Request Parameters:**
    - Path Variable: `{requestId}`
    - User ID is passed as a header `X-Sharer-User-Id`

#### Get Item Request
- **Method:** GET
- **Description:** Retrieves the details of all user's requests.
- **Request Parameters:**
    - User ID is passed as a header `X-Sharer-User-Id`

#### Post Comment
- **Method:** POST
- **Description:** Creating request for a specific item
- **Request Parameters:**
    - User ID is passed as a header `X-Sharer-User-Id`
    - Request Body `{ "description": "Leave a description here" }`

### Users `/users`

#### Get Users
- **Method:** GET
- **Description:** Retrieves the Collection of all existing users

#### Get User `/{id}`
- **Method:** GET
- **Description:** Retrieves the details of a specific user.
- **Request Parameters:**
  - Path Variable: `{id}`

#### Create User
- **Method:** POST
- **Description:** Creating new user
- **Request Parameters:**
    - Request Body
      `{ "name": "user Name",
      "email": "email@gmail.com" }`

#### Update User `/{id}`
- **Method:** PATCH
- **Description:** Updating name or email of existing user
- **Request Parameters:**
    - Request Body
      `{ "name": "user Updated Name",
      "email": "updated_email@gmail.com" }`
    - Path Variable `/{id}`

#### Delete User `/{id}`
- **Method:** DELETE
- **Description:** Deleting existing user
- **Request Parameters:**
    - Path Variable `/{id}`


## Technical Stack

### Spring Framework Integration

### RESTful API Development

### Database Integration
The project utilizes Spring Data JPA, a powerful ORM (Object-Relational Mapping)
framework, to integrate with databases. This project uses H2 database for in-memory development 
and testing.

### Maven Build Tool

### Containerization with Docker

### Orchestration with Docker Compose:

### Testing and Quality Assurance
The project utilizes Spring Testing Frameworks (JUnit, Mockito) 
to write comprehensive unit tests and perform integration testing.

