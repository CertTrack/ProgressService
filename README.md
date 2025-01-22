# Progress Service Documentation

## Overview
The Progress Service is responsible for tracking and managing users' progress in courses. It allows users to update their progress, retrieve progress data, and receive notifications upon course completion.

The service is built using Spring Boot and integrates with other services through Kafka for notifications. It also interacts with a database to store and retrieve progress information.

---

## Features
- Update progress for a specific user and course.
- Retrieve progress for a specific user and course.
- Get all progress records for a user.
- Clear progress for a specific user and course.
- Send notifications upon course completion.

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

---

## Security
The service uses Spring Security to ensure endpoints are secured. All requests require authentication, and only authenticated users can interact with the endpoints.

---

## Notification Integration
When a user completes a course (progressPercentage reaches 100%), the service sends a notification using Kafka. The notification includes the course name and a congratulatory message.

---

## Database Schema
The progress records are stored in the `progress` table with the following structure:

| Column Name        | Data Type   | Constraints                  |
|--------------------|-------------|------------------------------|
| `id`               | Long        | Primary Key                  |
| `user_id`          | Long        | Not Null                     |
| `course_id`        | Long        | Not Null                     |
| `progress_percentage` | Double   | Not Null                     |
| `last_updated`     | Timestamp   | Not Null                     |

---

## Configuration
The service runs on port **8084** and uses the following properties for security and integration:
- **Kafka:** For notification integration.
- **JdbcTemplate:** For database interaction.
- **Spring Security:** For securing endpoints.

---

## Example Usage
1. **Update Progress:**
   ```bash
   curl -X PUT "http://localhost:8084/progress/update?userId=1&courseId=101&progressPercentage=2"
   ```

2. **Get Progress:**
   ```bash
   curl -X GET "http://localhost:8084/progress/get?userId=1&courseId=101"
   ```

3. **Get All Progress:**
   ```bash
   curl -X GET "http://localhost:8084/progress/all?userId=1"
   ```

4. **Delete Progress:**
   ```bash
   curl -X DELETE "http://localhost:8084/progress/delete?userId=1&courseId=101"
   ```

---

## Contact
For further questions or support, please contact the development team.
