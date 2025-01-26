# Progress Service Documentation

The Progress Service is responsible for tracking and managing users' progress in courses. It allows users to update their progress, retrieve progress data, and receive notifications upon course completion.

The service is built using Spring Boot and integrates with other services through Kafka for notifications. It also interacts with a database to store and retrieve progress information.


## Features
- Update progress for a specific user and course.
- Retrieve progress for a specific user and course.
- Get all progress records for a user.
- Clear progress for a specific user and course.
- Send notifications upon course completion.

---

## Technologies Used

- **Spring Boot**: Core framework for the service.
- **Spring Security**: Provides authentication and authorization capabilities.
- **Auth0 (JWT)**: For Decoding JWT.
- **RESTful API Design**: Provides a clean and structured API interface.
- **Spring Kafka**: as a producer of notifications.
- **Spring Cloud Eureka Client**


---

## Security
The service uses Spring Security to ensure endpoints are secured. All requests require authentication, and only authenticated users can interact with the endpoints.

---
## Endpoints

### **Update Progress**
**URL:** `/progress/update`

**Method:** `PUT`

**Description:** Updates the progress percentage of a user for a specific course.

**Request Parameters:**
- `userId` (Long, required): The ID of the user.
- `courseId` (Long, required): The ID of the course.
- `progressPercentage` (int, required): The current module completed by the user.

**Response:**
- `200 OK`: Progress updated successfully.

---

### **Get Progress**
**URL:** `/progress/get`

**Method:** `GET`

**Description:** Retrieves the progress of a user for a specific course.

**Request Parameters:**
- `userId` (Long, required): The ID of the user.
- `courseId` (Long, required): The ID of the course.

**Response:**
- `200 OK`: Progress data in the following format:
```json
{
  "id": Long,
  "userId": Long,
  "courseId": Long,
  "progressPercentage": Double,
  "lastUpdated": String
}
```

---

### **Get All Progress**
**URL:** `/progress/all`

**Method:** `GET`

**Description:** Retrieves all progress records for a specific user.

**Request Parameters:**
- `userId` (Long, required): The ID of the user.

**Response:**
- `200 OK`: List of progress records in the following format:
```json
[
  {
    "id": Long,
    "userId": Long,
    "courseId": Long,
    "progressPercentage": Double,
    "lastUpdated": String
  }
]
```

---

### **Delete Progress**
**URL:** `/progress/delete`

**Method:** `DELETE`

**Description:** Deletes the progress of a user for a specific course.

**Request Parameters:**
- `userId` (Long, required): The ID of the user.
- `courseId` (Long, required): The ID of the course.

**Response:**
- `200 OK`: Progress deleted successfully.
- `404 NOT FOUND`: Progress record not found.


## Notification Integration
When a user completes a course (progressPercentage reaches 100%), the service sends a notification using Kafka. The notification includes the course name and a congratulatory message.

---

## Database Schema
The progress records are stored in the `progress` table with the following structure:
       Column        |          Type          | Collation | Nullable |             Default
---------------------|------------------------|-----------|----------|----------------------------------
 id                  | bigint                 |           | not null | generated by default as identity
 course_id           | bigint                 |           | not null |
 last_updated        | character varying(255) |           | not null |
 progress_percentage | double precision       |           | not null |
 user_id             | bigint                 |           | not null |

---

## Configuration
The service runs on port **8084** and uses the following properties for security and integration:
- **Kafka:** For notification integration.
- **JdbcTemplate:** For database interaction.
- **Spring Security:** For securing endpoints.
