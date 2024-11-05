package com.API.persistence.repository;

import com.API.dto.dtoResponse.UserResponseDto;
import com.API.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    <T extends UserResponseDto> T findUserByBadgeNumber(@Param("badgeNumber") String badgeNumber);
}
