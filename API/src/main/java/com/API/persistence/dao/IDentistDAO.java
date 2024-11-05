package com.API.persistence.dao;

import com.API.persistence.entities.userImpl.Dentist;

import java.util.List;
import java.util.Optional;

public interface IDentistDAO {

    void save(Dentist dentist);

    List<Dentist> findAll();

    Optional<Dentist> findById(Long id);

    void deleteById(Long id);


}
