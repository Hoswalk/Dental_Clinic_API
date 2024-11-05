package com.API.persistence.dao;

import com.API.persistence.entities.userImpl.Patient;

import java.util.List;
import java.util.Optional;

public interface IPatientDAO {

    void save(Patient patient);

    List<Patient> findAll();

    Optional<Patient> findById(Long id);

    void deleteById(Long id);
}
