package com.API.service;

import com.API.dto.dtoRequest.UserRequestDto;
import com.API.dto.dtoResponse.UserResponseDto;
import com.API.exception.ResourceNotFoundException;
import com.API.persistence.entities.User;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    //Metodo generico para guardar usuarios, recibe el dto, la clase dle usuario, y el prefijo para badgenumber
    <T extends User> T saveUser(UserRequestDto userRequestDto, Class<T> userClass, String badgePrefix);

    List<UserResponseDto> findAll();

    Optional<UserResponseDto> findUserById(Long id);

    //<T extends UserResponseDto> T findUserByBadgeNumber(String badgeNumber, Class<T> type);

    //Optional<String> findBadgeNumberByUser(Long userId);

    Optional<UserResponseDto> findUserByEmail(String email);

    void deleteUserById(Long id) throws ResolutionException, ResourceNotFoundException;

}
