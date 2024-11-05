package com.API.persistence.dao.impl;

import com.API.persistence.dao.IPatientDAO;
import com.API.persistence.entities.userImpl.Patient;
import com.API.persistence.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PatientDaoImpl implements IPatientDAO {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public void save(Patient patient) {
        patientRepository.save(patient);
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        patientRepository.deleteById(id);
    }
}
