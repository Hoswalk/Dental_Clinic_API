package com.API.service.impl;

import com.API.dto.dtoRequest.UserRequestDto;
import com.API.dto.dtoResponse.DentistResponseDto;
import com.API.dto.dtoResponse.UserResponseDto;
import com.API.exception.ResourceNotFoundException;
import com.API.persistence.entities.User;
import com.API.persistence.entities.userImpl.Dentist;
import com.API.persistence.entities.userImpl.Patient;
import com.API.persistence.repository.UserRepository;
import com.API.service.IUserService;
import com.API.utils.JsonPrinter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public <T extends User> T saveUser(UserRequestDto userRequestDto, Class<T> userClass, String badgePrefix) {

        //Mapeo del DTO al tipo de entidad correspondiente
        T userEntity = modelMapper.map(userRequestDto, userClass);

        //Generar badgeNumber
        if (userEntity.getBadgeNumber() == null || userEntity.getBadgeNumber().isEmpty()){
            userEntity.setBadgeNumber(generateBadgeNumber(badgePrefix));
        }

        //Guardar en el repo
        T saveUser = userRepository.save(userEntity);

        return saveUser;
    }


    @Override
    public List<UserResponseDto> findAll() {

        List<UserResponseDto> userResponseDtoList = userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();

        return userResponseDtoList;
    }

    @Override
    public Optional<UserResponseDto> findUserById(Long id) {

        User userSearched = userRepository.findById(id).orElse(null);
        UserResponseDto userFound = null;

        if (userSearched != null){
            userFound = modelMapper.map(userSearched, UserResponseDto.class);
            LOGGER.info("User found: {}", JsonPrinter.toString(userFound));
        } else LOGGER.error("User not found: {}", JsonPrinter.toString(userSearched));

        return Optional.of(userFound);
    }

    @Override
    public Optional<UserResponseDto> findUserByEmail(String email) {

        User userSearched = userRepository.findByEmail(email).orElse(null);
        UserResponseDto userFound = null;

        if (userSearched != null){
            userFound = modelMapper.map(userSearched, UserResponseDto.class);
            LOGGER.info("User found: {}", JsonPrinter.toString(userSearched));
        } else LOGGER.error("User not found: {}", JsonPrinter.toString(userSearched));

        return Optional.of(userFound);
    }

    @Override
    public void deleteUserById(Long id) throws ResourceNotFoundException {

        if (findUserById(id).isPresent()){
            userRepository.deleteById(id);
            LOGGER.warn("User with id: " + id + " has been deleted.");
        } else{
            throw new ResourceNotFoundException("User with id: " + id + " was not found.");
        }
    }

    //Metodo para generar badgeNumber con prefijo
    private String generateBadgeNumber(String prefix){
        return prefix + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 7);
    }
}
