# Dental_Clinic_API

Overview
The Dental Clinic API is a backend service for managing patients, dentists, and appointments within a dental clinic. It allows the management of patient and dentist records, as well as scheduling and managing appointments. The project is built using Spring Boot, JPA, and MySQL.

Features
- Patient Management: Create, update, and retrieve patient information.
- Dentist Management: Create, update, and retrieve dentist information.
- Appointment Management: Schedule, retrieve, and manage appointments.
- Basic CRUD operations: For managing patients, dentists, and appointments.

Technologies Used
- Java 17 (or compatible version)
- Spring Boot 3.x
- Spring Data JPA (for database interactions)
- MySQL (configured in the application.properties file)
- Maven (for dependency management)
- ModelMapper (for DTO mapping)

Project Structure
src/
 ├── main/
 │   ├── java/
 │   │   └── com/
 │   │       └── API/
 │   │           ├── config/           # API Configuration + Swagger config
 │   │           ├── controller/       # API Controllers
 │   │           ├── dto/              # DTOs
 │   │           ├── event/            # Event
 │   │           ├── exception/        # Exception
 │   │           ├── handler/          # Handler for event
 │   │           ├── persistence/      # JPA Entities / JPA Repositories / DAO
 │   │           ├── service/          # Service Layer
 │   ├── resources/
 │   │   └── application.properties    # Database & Application configuration
 ├── test/
 │   ├── java/
 │   │   └── com/
 │   │       └── API/
 │   │           ├── controller/       # Controller Layer Tests
 │   │           ├── mockData/         # Fixtures File
 │   │           ├── service/          # Service Layer Tests
 
Setup and Installation
Prerequisites

- Java 17 (or compatible version)
- Maven
- MySQL
  
1. Clone the Repository
git clone https://github.com/hoswalk/Dental_Clinic_API.git
cd Dental_Clinic_API

3. Configure Application Properties
Update the src/main/resources/application.properties file with your MySQL database connection settings.

Example for MySQL:

properties
spring.datasource.url=jdbc:mysql://localhost:3306/dentalclinic
spring.datasource.username=root
spring.datasource.password=rootpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect

3. Build the Project
Using Maven: mvn clean install
- mvn clean install

4. Testing the API
Use Postman, cURL, or any API client to test the following endpoints.

API Endpoints
Patient Endpoints

Create a New Patient
POST /api/patient/save
- Request Body: PatientRequestDto
- Response: PatientResponseDto

Get All Patients
GET /api/patient/findAll
- Response: List<PatientResponseDto>
  
Get Patient by ID
GET /api/patient/findById/{id}
- Request Param: Long id
- Response: PatientResponseDto
  
Get Patient by badge number
GET /api/patient/findByBadgeNumber/{badgeNumber}
- Request Param: Long id
- Response: PatientResponseDto

Put Patient update
PUT /api/patient/update/{id}
- Request Body: PatientRequestDto
- Request Param: Long id
- Response Body: PatientResponseDto

Delete Patient by ID
DELETE /api/patient/deleteById/{id}
- Request Param: Long id
- Response: Nothing

Dentist Endpoints

Create a New Dentist
POST /api/dentist/save
- Request Body: DentistRequestDto
- Response: DentistResponseDto

Get All Dentists
GET /api/dentist/findAll
Response: List<DentistResponseDto>

Get Dentist by ID
GET /api/dentist/findById/{id}
- Request Param: Long id
- Response: DentistResponseDto

Get Dentist by badge number
GET /api/dentist/findByBadgeNumber/{badgeNumber}
- Request Param: Long id
- Response: DentistResponseDto

Put Dentist update
PUT /api/dentist/update/{id}
- Request Param: Long id
- Request Body: DentistRequestDto
- Response: DentistResponseDto

Delete Patient by ID
DELETE /api/dentist/deleteById/{id}
- Request Param: Long id
- Response: Nothing

Appointment Endpoints

Create a New Appointment
POST /api/appointment/save
- Request Body: AppointmentRequestDto
- Response: AppointmentResponseDto

Get All Appointments
GET /api/appointment/findAll
- Response: List<AppointmentResponseDto>

Get Appointment by ID
GET /api/appointment/findById/{id}
- Request Param: Long id
- Response: AppointmentResponseDto

Put Appointment date and hour update
PUT /api/appointment/update/{id}
- Request Body: AppointmentRequestDto
- Request Param: Long id
- Response: AppointmentResponseDto

Put Appointments to new dentist
PUT /api/appointment/updateAppointmentsToNewDentist/{oldDentistId}/{newDentistId}
- Request Param: Long oldDentistId
- Request Param: Long newDentistId

Delete Appointment by ID
DELETE /api/appointment/deleteById/{id}
- Request Param: Long id
- Response: Nothing

Models and DTOs
Patient
- PatientRequestDto: Contains fields like firstName, lastName, dob, address, phoneNumber.
- PatientResponseDto: Contains details like id, firstName, lastName, dob, address, phoneNumber.

Dentist
- DentistRequestDto: For creating a new dentist, contains fields like firstName, lastName, specialty, phoneNumber.
- DentistResponseDto: For returning dentist details.

Appointment
- AppointmentRequestDto: Contains details for scheduling an appointment, such as date, patientId, dentistId.
- AppointmentResponseDto: Contains appointment details such as id, date, status, patientName, dentistName.

Error Handling
The application uses custom exception handling to respond with appropriate HTTP statuses for different errors:

404 - Not Found: For nonexistent patients, dentists, or appointments.
400 - Bad Request: For invalid data in requests.

Testing
Unit and integration tests are implemented using JUnit and Mockito. The tests cover:

Patient, dentist, and appointment CRUD operations.
Run tests using:
- mvn test

Conclusion
This Dental Clinic API provides a backend solution for managing patients, dentists, and appointments in a dental clinic. It offers essential CRUD operations for managing patient and dentist data, as well as scheduling and managing appointments.
