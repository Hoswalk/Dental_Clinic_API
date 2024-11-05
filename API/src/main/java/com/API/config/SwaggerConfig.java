package com.API.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Dentist Clinic API",
                description = """
                        This API provides comprehensive management of patient, dentist, and appointment records for a dental clinic. It supports CRUD operations (Create, Read, Update, Delete) for patients, dentists, and appointments, as well as additional features:
                        
                        Dentist Management: In addition to standard operations, the API includes an event-based reassignment feature that automatically reassigns appointments if a dentist is unavailable due to illness, termination, or other reasons.
                        
                        Badge Number Search: The API allows for searches by badge number for both dentists and patients, enabling quick identification and access to specific records.
                        This API is designed to streamline clinic operations by automating reassignment processes and providing quick access to essential information.""",
                contact = @Contact(
                        name = "Eder Vesga",
                        email = "edvesga0817@hotmail.com"
                ),
                version = "1.0.0",
                summary = "This API manages patients, dentists, and appointments for a dental clinic, supporting CRUD operations, badge number searches, and automatic reassignment of appointments when a dentist is unavailable."
        ),
        servers = {
                @Server(
                        description = "DEV SERVER",
                        url = "http://localhost:8080"
                )
        }
)
public class SwaggerConfig {
}
