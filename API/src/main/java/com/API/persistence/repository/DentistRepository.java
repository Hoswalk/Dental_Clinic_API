package com.API.persistence.repository;

import com.API.dto.dtoResponse.DentistResponseDto;
import com.API.persistence.entities.userImpl.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DentistRepository extends JpaRepository<Dentist, Long> {

    Optional<Dentist> findDentistByBadgeNumber(String badgeNumber);

    List<Dentist> findFirstByAvailableTrue();

    Dentist findFirstDentistByAvailableTrue();
}
