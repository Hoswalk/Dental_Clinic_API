package com.API.persistence.repository;

import com.API.dto.dtoResponse.PatientResponseDto;
import com.API.persistence.entities.userImpl.Dentist;
import com.API.persistence.entities.userImpl.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findPatientByBadgeNumber(String badgeNumber);
}
