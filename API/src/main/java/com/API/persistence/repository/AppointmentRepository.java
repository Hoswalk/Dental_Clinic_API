package com.API.persistence.repository;

import com.API.persistence.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByDentistId(Long dentistId);

    boolean existsByDentistIdAndAppointmentDateHour(Long dentistId, LocalDateTime appointmentDateAndHour);
}
