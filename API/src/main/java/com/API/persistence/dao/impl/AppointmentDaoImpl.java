package com.API.persistence.dao.impl;

import com.API.persistence.dao.IAppointmentDAO;
import com.API.persistence.entities.Appointment;
import com.API.persistence.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class AppointmentDaoImpl implements IAppointmentDAO {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        appointmentRepository.deleteById(id);
    }
}
