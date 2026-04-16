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




- Question: In your report, explain the default lifecycle of a JAX-RS Resource class. Is a
new instance instantiated for every incoming request, or does the runtime treat it as a
singleton? Elaborate on how this architectural decision impacts the way you manage and
synchronize your in-memory data structures (maps/lists) to prevent data loss or race con-
ditions.

JAX-RS resource classes are usually request-scoped meaning for each incoming HTTP request a new instance is created. This is better than a singleton because request=specific data is not shared between clients. However for this coursework all the data is stored in sharing in-memory structures such as hashmaps and arraylist. Even though the resouce itself is per-request the shared collections the shared collections can still be accessed by multiple requests all at the same time.

- Question: Why is the provision of ”Hypermedia” (links and navigation within responses)
considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach
benefit client developers compared to static documentation?

Hypermedia is important in REST because it allows the server to provide links to related resources inside responses. This means that clients can find the API dynamically instead of relying on static documentations or premade urls. In this API the discovery endpoint returns links to the main collections, such as rooms and sensors which makes it must easier to use but also making the structure easy changeable later but also easier to maintain.

- Question: When returning a list of rooms, what are the implications of returning only
IDs versus returning the full room objects? Consider network bandwidth and client side
processing.

Returning only IDS reduces the overall bandwith, but the client then needs extra requests to get the full data. Returning full objects increases the payload size, but it then gives all the information to the client and reduces the number of requests. Therefore, IDS are more efficient for smaller responses for the client. 

- Question: Is the DELETE operation idempotent in your implementation? Provide a detailed
justification by describing what happens if a client mistakenly sends the exact same DELETE
request for a room multiple times.

Yes, the DELETE is idemnpotent in the API as sending a DELETE request multiple times will not change the fact that the room has already been deleted. For example, if a room has already been successfully deleted, repeating the DELETE function will not delete it again as it has already been deleted meaning the first call changes the state, while the later Delete functions do not change it again.

- Question: We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on
the POST method. Explain the technical consequences if a client attempts to send data in
a different format, such as text/plain or application/xml. How does JAX-RS handle this
mismatch?

@Consumes(MediaType.APPLICATION_JSON) tells JAX-RS that the endpoint only accepts JSON request bodies. If a client sends another media type such as text/plain or application/xml, JAX-RS will reject the request because the method is not configured to consume that format. This ensures that the endpoint only processes data in the format it expects.

- Question: You implemented this filtering using @QueryParam. Contrast this with an alterna-
tive design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why
is the query parameter approach generally considered superior for filtering and searching
collections?

Query parameters are better for filtering because they represent optional search criteria applied to a collection. For example, /sensors?type=CO2 clearly means “return sensors, but only those matching this filter.”. Query parameters are therefore more  flexible, more readable and also more aligned with REST conventions for search and filtering.

- Question: Discuss the architectural benefits of the Sub-Resource Locator pattern. How
does delegating logic to separate classes help manage complexity in large APIs compared
to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive con-
troller class?

The Sub-Resource Locator pattern improves structure by delegating nested resource logic to a dedicated class. In this coursework, SensorResource handles sensors, while SensorReadingResource handles the readings of a specific sensor. This keeps the code easier to understand. In larger APIs, separating nested resources into different classes reduces complexity, improves maintainability, and makes each class responsible for a smaller and clearer part of the API.

- Question: Why is HTTP 422 often considered more semantically accurate than a standard
404 when the issue is a missing reference inside a valid JSON payload?

HTTP 422 is better because the request itself is valid and the JSON is correctly structured, but the server does not process it because of invalid meaning inside the payload. In this case, the client sends a valid sensor object, but the roomId refers to a room that does not exist. A 404 normally means the requested URI itself was not found, while 422 better describes a semantic validation failure inside the request data.  

- Question: From a cybersecurity standpoint, explain the risks associated with exposing
internal Java stack traces to external API consumers. What specific information could an
attacker gather from such a trace?

Exposing Java stack traces is dangerous because it reveals internal implementation details of the system. An attacker could learn package names, class names, method names. This information can help them understand the principal of the application and identify possible weaknesses or attack points. For this reason, a generic 500 response is safer than exposing raw internal error details.

- Question: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like
logging, rather than manually inserting Logger.info() statements inside every single re-
source method?

JAX-RS filters are better for logging because logging is a cross-cutting concern that applies to many endpoints. A filter allows the logging logic to be written once and applied automatically to every request and response. This avoids repeating logging code in every resource method, keeps the resource classes cleaner and makes the API easier to maintain.
