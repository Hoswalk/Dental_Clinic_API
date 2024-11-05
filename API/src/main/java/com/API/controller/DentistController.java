package com.API.controller;

import com.API.dto.dtoRequest.DentistRequestDto;
import com.API.dto.dtoResponse.DentistResponseDto;
import com.API.exception.ResourceNotFoundException;
import com.API.service.impl.DentistServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dentist")
@AllArgsConstructor
public class DentistController {

    private DentistServiceImpl dentistService;

    //POST
    @PostMapping("/save")
    public ResponseEntity<DentistResponseDto> saveDentist(@RequestBody @Valid DentistRequestDto dentistRequestDto){
        return new ResponseEntity<>(dentistService.saveDentist(dentistRequestDto), HttpStatus.CREATED);
    }

    //GET
    @GetMapping("/findAll")
    public ResponseEntity<List<DentistResponseDto>> findAllDentist(){
        return new ResponseEntity<>(dentistService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return new ResponseEntity<>(dentistService.findDentistById(id), HttpStatus.OK);
    }

    @GetMapping("/findByBadgeNumber/{badgeNumber}")
    public ResponseEntity<?> findByBadgeNumber(@PathVariable String badgeNumber){
        return new ResponseEntity<>(dentistService.findDentistByBadgeNumber(badgeNumber), HttpStatus.OK);
    }

    //PUT
    @PutMapping("/update/{id}")
    public ResponseEntity<DentistResponseDto> updateDentist(@RequestBody DentistRequestDto dentistRequestDto, @PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(dentistService.updateDentist(dentistRequestDto, id), HttpStatus.OK);
    }

    @PutMapping("/unavailable/{dentistId}")
    public ResponseEntity<?> markDentistAsUnavailable(@PathVariable Long dentistId, @RequestBody String reason){
        dentistService.markDentistAsUnavailable(dentistId, reason);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //DELETE
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById (@PathVariable Long id) throws ResourceNotFoundException {
        dentistService.deleteDentistById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
