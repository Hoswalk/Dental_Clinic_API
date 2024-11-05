package com.API.persistence.dao;

import com.API.persistence.entities.Appointment;

import java.util.List;
import java.util.Optional;

public interface IAppointmentDAO {

    void save(Appointment appointment);

    List<Appointment> findAll();

    Optional<Appointment> findById(Long id);

    void deleteById(Long id);
}
