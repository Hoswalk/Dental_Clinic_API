package com.API.persistence.dao;

import com.API.persistence.entities.Address;

import java.util.List;
import java.util.Optional;

public interface IAddressDAO {

    void save(Address address);

    List<Address> findAll();

    Optional<Address> findById(Long id);

    void deleteById(Long id);
}
