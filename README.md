The Smart Campus Sensor and Room Management API i created is a web service that manages both rooms and environmental sensors across a university campus.
This API allows to:

.Create and retrieve rooms

.Register sensors assigned to rooms

.Filter sensors by type

.Store and retrieve historical sensor readings

.Return structured error responses using HTTP status codes

.Log API requests and responses and observablility

This API was made using JAX-RS and deployed on Apache Tomcat. All data is stored in memory using HashMaps and ArrayList, as required by the coursework specification.

What i used to make it:

Java

Jax-RS

Apache Tomcat

NetBeans

Postman

GitHub

Api base URL: http://localhost:8080/Coursework/api/v1

How to build and run the project:

1.Clone the repository from github: git clone https://github.com/YOUR_USERNAME/smart-campus-api.git

2.Open the project: Open the project in NetBeans IDE.

3. Build the project: Right-cluck the project and select clean and build

4. Run the project/ The API will then start using Apache Tomcat.

How to Discover the Endpoint: GET /api/v1

Room Management:

Get all rooms = GET /api/v1/rooms

Create Rooms:

POST /api/v1/rooms

//
{
"id":"LIB-301",
"name":"Library",
"capacity":40
}
//

Get a room by ID:

GET /api/v1/rooms/{roomId}

Delete a room:

Delete /api/v1/rooms/{roomId}

If the room contains sensors the api will return 409 conflict

Sensor Management

Create a sensor:

POST /api/v1/sensors

//
{
"id":"CO2-001",
"type":"CO2",
"status":"ACTIVE",
"currentValue":420.5,
"roomId":"LIB-301"
}
//

If the room does not exist the API will return: 422 unprocessable Entity

Get Sensors:

GET /api/v1/sensors

Filter Sensors by type:

GET /api/v1/sensors?type=CO2

This will allow client to retrieve sensors of a specific type

Sensor Readings (Sub-Resource)

Get sensors readings:

GET /api/v1/sensors/{sensorId}/readings

Add a sensor reading:

POST /api/v1/sensors/{sensorId}/readings

Example:

//
{
"id":"R-001",
"timestamp":1710000000,
"value":435.7
}
//

When a reading is added to the sensors, the currentvalue updated automatically

Example of curl commands:

//
API discovery : curl http://localhost:8080/Coursework/api/v1
//

Create Room

//
curl -X POST http://localhost:8080/Coursework/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"LIB-301","name":"Library","capacity":40}'
//

Get rooms:

//
curl http://localhost:8080/Coursework/api/v1/rooms
//

Create sensor:

//
curl -X POST http://localhost:8080/Coursework/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"CO2-001","type":"CO2","status":"ACTIVE","currentValue":420.5,"roomId":"LIB-301"}'
//

Add sensor reading

//
curl -X POST http://localhost:8080/Coursework/api/v1/sensors/CO2-001/readings \
-H "Content-Type: application/json" \
-d '{"id":"R-001","timestamp":1710000000,"value":435.7}'
//

All exception handling errors:

| HTTP Status Code | Error Name            | Scenario                                                         | Example Endpoint                         |
| ---------------- | --------------------- | ---------------------------------------------------------------- | ---------------------------------------- |
| **409**          | Conflict              | Attempting to delete a room that still contains active sensors   | `DELETE /api/v1/rooms/LIB-301`           |
| **422**          | Unprocessable Entity  | Creating a sensor with a roomId that does not exist              | `POST /api/v1/sensors`                   |
| **403**          | Forbidden             | Attempting to add a reading to a sensor in MAINTENANCE state     | `POST /api/v1/sensors/TEMP-999/readings` |
| **500**          | Internal Server Error | Unexpected runtime errors handled by the global exception mapper | Any endpoint                             |








