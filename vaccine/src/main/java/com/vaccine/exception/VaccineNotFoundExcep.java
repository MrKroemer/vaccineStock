package com.vaccine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VaccineNotFoundExcep extends Exception {

    public VaccineNotFoundExcep(String instrumentName){
        super(String.format("Instrument with name %s not found in the system.", instrumentName));
    }

    public VaccineNotFoundExcep(Long id) { super(String.format("Instrument with id %s not found in the system.", id));}
}
