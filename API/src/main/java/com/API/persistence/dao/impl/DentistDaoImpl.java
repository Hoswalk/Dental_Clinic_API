package com.API.persistence.dao.impl;

import com.API.persistence.dao.IDentistDAO;
import com.API.persistence.entities.userImpl.Dentist;
import com.API.persistence.repository.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DentistDaoImpl implements IDentistDAO {

    @Autowired
    private DentistRepository dentistRepository;

    @Override
    public void save(Dentist dentist) {
        dentistRepository.save(dentist);
    }

    @Override
    public List<Dentist> findAll() {
        return dentistRepository.findAll();
    }

    @Override
    public Optional<Dentist> findById(Long id) {
        return dentistRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        dentistRepository.deleteById(id);
    }
}
